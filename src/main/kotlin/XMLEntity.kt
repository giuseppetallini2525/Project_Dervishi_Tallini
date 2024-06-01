import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathFactory

/**
 * Represents an XML Entity (tag) with nested entities, attributes, and text.
 *
 * @property name The name of the XML entity.
 * @property attributes The attributes of the XML entity.
 * @property children The child entities of this XML entity.
 * @property textContent The text content of the XML entity.
 * @property parent The parent entity of this XML entity.
 * @property depth The depth of this entity in the XML tree.
 */
class XMLEntity(var name: String) {
    private val attributes = mutableMapOf<String, String>()
    private val children = mutableListOf<XMLEntity>()
    var textContent: String? = null
    var parent: XMLEntity? = null
        private set
    var depth: Int = 0

    /**
     * Adds or updates an attribute.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     */
    fun setAttribute(name: String, value: String) {
        attributes[name] = value
    }

    /**
     * Returns the attributes of the entity.
     *
     * @return A mutable map of the attributes.
     */
    fun getAttributes(): MutableMap<String, String> = attributes

    /**
     * Removes an attribute.
     *
     * @param name The name of the attribute to remove.
     */
    fun removeAttribute(name: String) {
        attributes.remove(name)
    }

    /**
     * Adds a child entity.
     *
     * @param child The child entity to add.
     */
    fun addChild(child: XMLEntity) {
        children.add(child)
        child.parent = this
    }

    /**
     * Removes a child entity.
     *
     * @param child The child entity to remove.
     */
    fun removeChild(child: XMLEntity) {
        if (children.remove(child)) {
            child.parent = null
        }
    }

    /**
     * Returns the children of the entity.
     *
     * @return A mutable list of child entities.
     */
    fun gtChildren(): MutableList<XMLEntity> = children

    /**
     * Returns the parent of the entity.
     *
     * @return The parent entity, or null if this is a root entity.
     */
    fun gtParent(): XMLEntity? = parent

    /**
     * Accepts a visitor to this entity.
     *
     * @param visitor The visitor to accept.
     */
    fun accept(visitor: XMLEntityVisitor) {
        if (visitor.visit(this)) {
            children.forEach {
                it.depth = this.depth + 1
                it.accept(visitor)
            }
        }
        visitor.endVisit(this)
    }

    /**
     * Queries the XML entity and its children based on a path.
     *
     * @param path The query path.
     * @return A list of matching XML entities.
     */
    fun query(path: String): List<XMLEntity> {
        val segments = path.split("/")
        return if (segments.first() == name) {
            query(segments.drop(1))
        } else {
            listOf()
        }
    }

    /**
     * Helper function to recursively query the XML entity and its children based on segments.
     *
     * @param segments The list of path segments.
     * @return A list of matching XML entities.
     */
    private fun query(segments: List<String>): List<XMLEntity> {
        if (segments.isEmpty()) return listOf(this)
        val segment = segments.first()
        val matchingChildren = children.filter { it.name == segment }
        return matchingChildren.flatMap { it.query(segments.drop(1)) }
    }
}









