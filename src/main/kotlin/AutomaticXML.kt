
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Annotation to specify a custom name for an XML element or attribute.
 *
 * @property name The custom name for the XML element or attribute.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlName(val name: String)

/**
 * Annotation to mark a property as an XML attribute.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlAttribute

/**
 * Annotation to exclude a property from XML serialization.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlExclude

/**
 * Annotation to specify a string transformer for an XML element.
 *
 * @property transformer The class of the string transformer.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlString(val transformer: KClass<out StringTransformer>)

/**
 * Annotation to specify an adapter for XML post-processing.
 *
 * @property adapter The class of the XML post-mapper.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlAdapter(val adapter: KClass<out XmlPostMapper>)

/**
 * Interface for transforming strings.
 */
interface StringTransformer {
    /**
     * Transforms the input string.
     *
     * @param input The input string.
     * @return The transformed string.
     */
    fun transform(input: String): String
}

/**
 * Interface for post-processing XML entities.
 */
interface XmlPostMapper {
    /**
     * Adapts an XML entity.
     *
     * @param entity The XML entity to adapt.
     * @return The adapted XML entity.
     */
    fun adapt(entity: XMLEntity): XMLEntity
}

/**
 * Converts an object to an XMLEntity.
 *
 * @param obj The object to convert.
 * @return The XMLEntity representing the object.
 */
fun toXMLEntity(obj: Any): XMLEntity {
    val kClass = obj::class
    val classNameAnnotation = kClass.findAnnotation<XmlName>()
    val className = classNameAnnotation?.name ?: kClass.simpleName!!.lowercase()
    val xmlEntity = XMLEntity(className)
    val primaryConstructor = kClass.primaryConstructor!!
    val properties = primaryConstructor.parameters.map { param ->
        kClass.members.find { it.name == param.name }!!
    }
    val attributeProperties = properties.filter { it.findAnnotation<XmlAttribute>() != null }
    val elementProperties = properties.filter { it.findAnnotation<XmlAttribute>() == null && it.findAnnotation<XmlExclude>() == null }

    for (property in attributeProperties) {
        val xmlName = property.findAnnotation<XmlName>()?.name ?: property.name
        var value = property.call(obj).toString()
        val transformerClass = property.findAnnotation<XmlString>()?.transformer
        val transformer = transformerClass?.objectInstance ?: transformerClass?.constructors?.first()?.call()
        if (transformer is StringTransformer) {
            value = transformer.transform(value)
        }
        xmlEntity.setAttribute(xmlName, value)
    }

    for (property in elementProperties) {
        val xmlName = property.findAnnotation<XmlName>()?.name ?: property.name.lowercase()
        val value = property.call(obj)

        if (value != null) {
            if (value is List<*>) {
                for (item in value) {
                    xmlEntity.addChild(toXMLEntity(item!!))
                }
            } else {
                var text = value.toString()
                val transformerClass = property.findAnnotation<XmlString>()?.transformer
                val transformer = transformerClass?.objectInstance ?: transformerClass?.constructors?.first()?.call()
                if (transformer is StringTransformer) {
                    text = transformer.transform(text)
                }
                val childEntity = XMLEntity(xmlName)
                childEntity.textContent = text
                xmlEntity.addChild(childEntity)
            }
        }
    }

    val adapterClass = kClass.findAnnotation<XmlAdapter>()?.adapter
    if (adapterClass != null) {
        val adapter = adapterClass.objectInstance ?: adapterClass.constructors.first().call()
        return (adapter as XmlPostMapper).adapt(xmlEntity)
    }

    return xmlEntity
}

/**
 * Annotation example classes and main function.
 */
@XmlName("componente")
data class ComponenteAvaliacao(
    @XmlAttribute @XmlName("nome") val nome: String,
    @XmlAttribute @XmlString(AddPercentage::class) val peso: Int
)

@XmlName("fuc")
data class FUC(
    @XmlAttribute @XmlName("codigo") val codigo: String,
    val nome: String,
    val ects: Double,
    @XmlExclude val observacoes: String,
    val avaliacao: List<ComponenteAvaliacao>
)

@XmlName("catalog")
@XmlAdapter(WineAdapter::class)
data class Catalog(
    @XmlExclude val description: String,
    val region: List<Region>
)

@XmlName("region")
data class Region(
    @XmlAttribute val name: String,
    val vineyard: List<Vineyard>
)

@XmlName("vineyard")
data class Vineyard(
    @XmlAttribute val name: String,
    val wine: List<Wine>
)

@XmlName("wine")
data class Wine(
    @XmlAttribute val name: String
)

/**
 * A string transformer that adds a percentage sign to the input string.
 */
class AddPercentage : StringTransformer {
    override fun transform(input: String): String {
        return "$input%"
    }
}

/**
 * A post-mapper that sorts wine elements alphabetically by their 'name' attribute.
 */
class WineAdapter : XmlPostMapper {
    override fun adapt(entity: XMLEntity): XMLEntity {
        for (child in entity.gtChildren()) {
            if (child.name == "region") {
                for (vineyard in child.gtChildren().filter { it.name == "vineyard" }) {
                    val wineChildren = vineyard.gtChildren().filter { it.name == "wine" }.toMutableList()
                    wineChildren.sortBy { it.getAttributes()["name"] }
                    vineyard.gtChildren().removeIf { it.name == "wine" }
                    vineyard.gtChildren().addAll(wineChildren)
                }
            }
        }
        return entity
    }
}

/**
 * The main function demonstrating the creation and manipulation of XML entities and documents.
 */
fun main() {
    val c = ComponenteAvaliacao("Quizzes", 20)

    val f = FUC(
        "M4310",
        "Programação Avançada",
        6.0,
        "la la...",
        listOf(
            ComponenteAvaliacao("Quizzes", 20),
            ComponenteAvaliacao("Projeto", 80)
        )
    )

    val catalog = Catalog("PeppeEni", listOf(
        Region("lazio", listOf(Vineyard("frascati", listOf(Wine("gotto d'oro"))))),
        Region("veneto", listOf(Vineyard("valpolicella", listOf(Wine("ripasso"), Wine("amarone")))))
    ))

    val ct = toXMLEntity(catalog)
    val adder = GlobalAttributeAdder("wine", "alcohol", "12%")
    ct.accept(adder)

    val renameVisitor = RenameEntityVisitor("wine", "ItalianWines")
    ct.accept(renameVisitor)

    val renameAttVisitor = RenameAttributeVisitor("region", "name", "realName")
    ct.accept(renameAttVisitor)

    val visitor = RemoveEntityVisitor("ItalianWines")
    ct.accept(visitor)

    val removeAttributeVisitor = RemoveAttributeVisitor("vineyard", "name")
    ct.accept(removeAttributeVisitor)

    val printer = PrettyPrintVisitor()
    ct.accept(printer)
    println(printer.getPrettyPrint())
}