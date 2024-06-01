import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Test class for automatic XML conversion and manipulation using various visitors.
 */
class TestAutomaticXML {
    val catalog = Catalog("PeppeEni", listOf(
        Region("lazio", listOf(Vineyard("frascati", listOf(Wine("gotto d'oro"))))),
        Region("veneto", listOf(Vineyard("valpolicella", listOf(Wine("ripasso"), Wine("amarone")))))
    ))

    /**
     * Tests the conversion of a Catalog object to XMLEntity and ensures the XML is printed as expected.
     */
    @Test
    fun testToXMLEntity() {
        // Convert to XMLEntity
        val xmlEntity = toXMLEntity(catalog)
        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        // Expected XML string
        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati">
                  <wine name="gotto d'oro"/>
                </vineyard>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella">
                  <wine name="amarone"/>
                  <wine name="ripasso"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        // Assert the XML output, testing the WineAdapter which sorts the wines
        assertEquals(expectedXml, printer.getPrettyPrint(), "The XML is not printed as expected")
    }

    /**
     * Tests the GlobalAttributeAdder to automatically add an attribute to XML entities.
     */
    @Test
    fun testGlobalAttributeAdderAutomatic() {
        val toAdd = GlobalAttributeAdder("vineyard", "location", "castelli romani")
        val xmlEntity = toXMLEntity(catalog)
        xmlEntity.accept(toAdd)
        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati" location="castelli romani">
                  <wine name="gotto d'oro"/>
                </vineyard>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella" location="castelli romani">
                  <wine name="amarone"/>
                  <wine name="ripasso"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint(), "The attribute was not added")
    }

    /**
     * Tests the RemoveAttributeVisitor to automatically remove an attribute from XML entities.
     */
    @Test
    fun testRemoveAttributeVisitorAutomatic() {
        val xmlEntity = toXMLEntity(catalog)
        val toRemove = RemoveAttributeVisitor("vineyard", "location")
        xmlEntity.accept(toRemove)

        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati">
                  <wine name="gotto d'oro"/>
                </vineyard>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella">
                  <wine name="amarone"/>
                  <wine name="ripasso"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint(), "The attribute was not removed")
    }

    /**
     * Tests the RemoveEntityVisitor to automatically remove XML entities.
     */
    @Test
    fun testRemoveEntityVisitorAutomatic() {
        val xmlEntity = toXMLEntity(catalog)

        // Apply RemoveEntityVisitor to remove all "wine" elements
        val toRemove = RemoveEntityVisitor("wine")
        xmlEntity.accept(toRemove)

        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati"/>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella"/>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint(), "The entity was not removed")
    }

    /**
     * Tests the RenameEntityVisitor to automatically rename XML entities.
     */
    @Test
    fun testRenameEntityVisitorAutomatic() {
        val xmlEntity = toXMLEntity(catalog)

        // Apply RenameEntityVisitor to rename all "wine" elements to "italianWine"
        val renameVisitor = RenameEntityVisitor("wine", "italianWine")
        xmlEntity.accept(renameVisitor)

        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati">
                  <italianWine name="gotto d'oro"/>
                </vineyard>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella">
                  <italianWine name="amarone"/>
                  <italianWine name="ripasso"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint(), "The entity was not renamed")
    }

    /**
     * Tests the RenameAttributeVisitor to automatically rename attributes in XML entities.
     */
    @Test
    fun testRenameAttributeVisitorAutomatic() {
        // Add a temporary attribute to demonstrate renaming
        val toAdd = GlobalAttributeAdder("vineyard", "location", "castelli romani")
        val xmlEntity = toXMLEntity(catalog)
        xmlEntity.accept(toAdd)

        // Apply RenameAttributeVisitor to rename the "location" attribute to "area"
        val renameAttributeVisitor = RenameAttributeVisitor("vineyard", "location", "area")
        xmlEntity.accept(renameAttributeVisitor)

        val printer = PrettyPrintVisitor()
        xmlEntity.accept(printer)

        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati" area="castelli romani">
                  <wine name="gotto d'oro"/>
                </vineyard>
              </region>
              <region name="veneto">
                <vineyard name="valpolicella" area="castelli romani">
                  <wine name="amarone"/>
                  <wine name="ripasso"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint(), "The attribute was not renamed")
    }
}

