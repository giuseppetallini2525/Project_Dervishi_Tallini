import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test class for testing various visitors on an XML structure.
 */
class TestVisitor {

    val wine_1 = XMLEntity("wine").apply { textContent = "Frascati Superiore" }
    val wine_2 = XMLEntity("wine").apply { textContent = "Santa Teresa" }
    val wine_3 = XMLEntity("wine").apply { textContent = "Marino Bianco" }
    val wine_4 = XMLEntity("wine").apply { textContent = "Marino Superiore" }

    val vineyard_1 = XMLEntity("vineyard").apply {
        setAttribute("name", "Fontana Candida")
        addChild(wine_1)
        addChild(wine_2)
    }

    val vineyard_2 = XMLEntity("vineyard").apply {
        setAttribute("name", "Gotto d'Oro")
        addChild(wine_3)
        addChild(wine_4)
    }

    val region = XMLEntity("region").apply {
        setAttribute("name", "Lazio")
        addChild(vineyard_1)
        addChild(vineyard_2)
    }

    val catalog = XMLEntity("catalog").apply {
        addChild(region)
    }

    /**
     * Tests the PrettyPrintVisitor to ensure XML is formatted correctly.
     */
    @Test
    fun testPrettyPrintVisitor() {
        val expected = """
    <catalog>
      <region name="Lazio">
        <vineyard name="Fontana Candida">
          <wine>Frascati Superiore</wine>
          <wine>Santa Teresa</wine>
        </vineyard>
        <vineyard name="Gotto d'Oro">
          <wine>Marino Bianco</wine>
          <wine>Marino Superiore</wine>
        </vineyard>
      </region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }

    /**
     * Tests the GlobalAttributeAdder to automatically add an attribute to XML entities.
     */
    @Test
    fun testGlobalAttributeAdder() {
        val toAdd = GlobalAttributeAdder("vineyard", "location", "castelli romani")
        catalog.accept(toAdd)
        val expected = """
    <catalog>
      <region name="Lazio">
        <vineyard name="Fontana Candida" location="castelli romani">
          <wine>Frascati Superiore</wine>
          <wine>Santa Teresa</wine>
        </vineyard>
        <vineyard name="Gotto d'Oro" location="castelli romani">
          <wine>Marino Bianco</wine>
          <wine>Marino Superiore</wine>
        </vineyard>
      </region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }

    /**
     * Tests the RemoveEntityVisitor to automatically remove XML entities.
     */
    @Test
    fun testRemoveEntityVisitor() {
        val toRemove = RemoveEntityVisitor("wine")
        catalog.accept(toRemove)
        val expected = """
    <catalog>
      <region name="Lazio">
        <vineyard name="Fontana Candida"/>
        <vineyard name="Gotto d'Oro"/>
      </region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }

    /**
     * Tests the RemoveAttributeVisitor to automatically remove an attribute from XML entities.
     */
    @Test
    fun testRemoveAttributeVisitor() {
        val remAtt = RemoveAttributeVisitor("region", "name")
        catalog.accept(remAtt)
        val expected = """
    <catalog>
      <region>
        <vineyard name="Fontana Candida">
          <wine>Frascati Superiore</wine>
          <wine>Santa Teresa</wine>
        </vineyard>
        <vineyard name="Gotto d'Oro">
          <wine>Marino Bianco</wine>
          <wine>Marino Superiore</wine>
        </vineyard>
      </region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }

    /**
     * Tests the RenameAttributeVisitor to automatically rename attributes in XML entities.
     */
    @Test
    fun testRenameAttributeVisitor() {
        val renAtt = RenameAttributeVisitor("region", "name", "realname")
        catalog.accept(renAtt)
        val expected = """
    <catalog>
      <region realname="Lazio">
        <vineyard name="Fontana Candida">
          <wine>Frascati Superiore</wine>
          <wine>Santa Teresa</wine>
        </vineyard>
        <vineyard name="Gotto d'Oro">
          <wine>Marino Bianco</wine>
          <wine>Marino Superiore</wine>
        </vineyard>
      </region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }

    /**
     * Tests the RenameEntityVisitor to automatically rename XML entities.
     */
    @Test
    fun testRenameEntityVisitor() {
        val renEntVis = RenameEntityVisitor("region", "Region")
        catalog.accept(renEntVis)
        val expected = """
    <catalog>
      <Region name="Lazio">
        <vineyard name="Fontana Candida">
          <wine>Frascati Superiore</wine>
          <wine>Santa Teresa</wine>
        </vineyard>
        <vineyard name="Gotto d'Oro">
          <wine>Marino Bianco</wine>
          <wine>Marino Superiore</wine>
        </vineyard>
      </Region>
    </catalog>
""".trimIndent()
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        assertEquals(expected, printer.getPrettyPrint())
    }
}