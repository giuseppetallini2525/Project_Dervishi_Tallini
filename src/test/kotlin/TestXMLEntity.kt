import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test class for testing the XMLEntity class and its various functionalities.
 */
class TestXMLEntity {

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
        addChild(vineyard_1)
        addChild(vineyard_2)
    }

    val catalog = XMLEntity("catalog").apply {
        addChild(region)
    }

    /**
     * Tests adding a child entity to an XMLEntity.
     */
    @Test
    fun testAddChild() {
        val expectedChildren = listOf(vineyard_1, vineyard_2)
        assertEquals(expectedChildren, region.gtChildren(), "The list of children should match the expected list")
    }

    /**
     * Tests removing a child entity from an XMLEntity.
     */
    @Test
    fun testRemoveChild() {
        region.removeChild(vineyard_2)
        val expectedChildren = listOf(vineyard_1)
        assertEquals(expectedChildren, region.gtChildren(), "The list of children should match the expected list")
    }

    /**
     * Tests setting an attribute for an XMLEntity.
     */
    @Test
    fun testSetAttribute() {
        wine_1.setAttribute("Alcohol_grade", "12")
        val expectedAttribute = mapOf("Alcohol_grade" to "12")
        assertEquals(expectedAttribute, wine_1.getAttributes())
    }

    /**
     * Tests removing an attribute from an XMLEntity.
     */
    @Test
    fun testRemoveAttribute() {
        val wine = XMLEntity("wine")
        wine.setAttribute("Alcohol_grade", "12")
        wine.setAttribute("Region", "Lazio")

        // Remove the 'Alcohol_grade' attribute
        wine.removeAttribute("Alcohol_grade")

        // Expected attributes after removal
        val expectedAttributes = mapOf("Region" to "Lazio")

        // Assert that the attributes map no longer contains 'Alcohol_grade'
        assertFalse(wine.getAttributes().containsKey("Alcohol_grade"), "Alcohol_grade should be removed from attributes.")
        assertEquals(expectedAttributes, wine.getAttributes(), "Attributes should match the expected values after removal.")
    }

    /**
     * Tests querying the XML structure using a micro XPath-like syntax.
     */
    @Test
    fun testMicroXPath() {
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        val queryPrinter = PrettyPrintVisitor()
        queryPrinter.queryMode = true
        val components = catalog.query("catalog/region/vineyard")
        queryPrinter.printQueryResult(components)
        val expected = """
    <vineyard name="Fontana Candida">
      <wine>Frascati Superiore</wine>
      <wine>Santa Teresa</wine>
    </vineyard>
    <vineyard name="Gotto d'Oro">
      <wine>Marino Bianco</wine>
      <wine>Marino Superiore</wine>
    </vineyard>
""".trimIndent()
        assertEquals(expected, queryPrinter.getPrettyPrint(), "The Subtree is not the one expected")
    }
}