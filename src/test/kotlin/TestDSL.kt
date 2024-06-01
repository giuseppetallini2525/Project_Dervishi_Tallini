import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Test class for testing the creation and manipulation of XML structures using the XMLEntity class.
 */
class XMLEntityTest {

    /**
     * Tests the creation of an XML structure and verifies its content.
     */
    @Test
    fun testXmlCreation() {
        // Creazione della struttura XML
        val catalog = xml("catalog") {
            element("region") {
                attr("name", "lazio")
                element("vineyard") {
                    attr("name", "frascati")
                    element("wine") {
                        attr("name", "gotto d'oro")
                    }
                }
            }
            element("region") {
                attr("name", "veneto")
                element("vineyard") {
                    attr("name", "valpolicella")
                    element("wine") {
                        attr("name", "ripasso")
                    }
                    element("wine") {
                        attr("name", "amarone")
                    }
                }
            }
        }

        // Verifica della struttura XML
        assertEquals("catalog", catalog.name)

        val lazioRegion = catalog["lazio"]
        assertNotNull(lazioRegion)
        assertEquals("lazio", lazioRegion?.getAttributes()?.get("name"))

        val venetoRegion = catalog["veneto"]
        assertNotNull(venetoRegion)
        assertEquals("veneto", venetoRegion?.getAttributes()?.get("name"))

        val valpolicellaVineyard = venetoRegion?.get("valpolicella")
        assertNotNull(valpolicellaVineyard)
        assertEquals("valpolicella", valpolicellaVineyard?.getAttributes()?.get("name"))

        val amaroneWine = valpolicellaVineyard?.get("amarone")
        assertNotNull(amaroneWine)
        assertEquals("amarone", amaroneWine?.getAttributes()?.get("name"))
    }

    /**
     * Tests the iteration over child elements of an XML structure.
     */
    @Test
    fun testElementsIteration() {
        // Creazione della struttura XML
        val catalog = xml("catalog") {
            element("region") {
                attr("name", "lazio")
                element("vineyard") {
                    attr("name", "frascati")
                    element("wine") {
                        attr("name", "gotto d'oro")
                    }
                }
            }
        }

        val lazioRegion = catalog / "region"
        assertNotNull(lazioRegion)

        val elementsNames = mutableListOf<String>()
        lazioRegion elements {
            elementsNames.add(it.name)
        }

        assertEquals(listOf("vineyard"), elementsNames)
    }

    /**
     * Tests the pretty-printing of an XML structure.
     */
    @Test
    fun testPrettyPrint() {
        // Creazione della struttura XML
        val catalog = xml("catalog") {
            element("region") {
                attr("name", "lazio")
                element("vineyard") {
                    attr("name", "frascati")
                    element("wine") {
                        attr("name", "gotto d'oro")
                    }
                }
            }
        }

        // Stampa della struttura XML
        val printer = PrettyPrintVisitor()
        catalog.accept(printer)
        val expectedXml = """
            <catalog>
              <region name="lazio">
                <vineyard name="frascati">
                  <wine name="gotto d'oro"/>
                </vineyard>
              </region>
            </catalog>
        """.trimIndent()

        assertEquals(expectedXml, printer.getPrettyPrint())
    }
}