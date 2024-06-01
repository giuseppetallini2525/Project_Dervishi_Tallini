
/**
 * Function to create a root XML element.
 *
 * @param name The name of the root element.
 * @param build A lambda to build the XML structure.
 * @return The root XMLEntity created.
 */
fun xml(name: String, build: XMLEntity.() -> Unit): XMLEntity {
    return XMLEntity(name).apply(build)
}

/**
 * Extension function for XMLEntity to create child elements.
 *
 * @param name The name of the child element.
 * @param build A lambda to build the child element.
 * @return The child XMLEntity created.
 */
fun XMLEntity.element(name: String, build: XMLEntity.() -> Unit): XMLEntity {
    return XMLEntity(name).apply {
        this@element.addChild(this)
        build(this)
    }
}

/**
 * Extension function for XMLEntity to add attributes.
 *
 * @param name The name of the attribute.
 * @param value The value of the attribute.
 */
fun XMLEntity.attr(name: String, value: String) {
    this.setAttribute(name, value)
}

/**
 * Extension function for XMLEntity to add text content.
 *
 * @param content The text content to add.
 */
fun XMLEntity.text(content: String) {
    this.textContent = content
}

/**
 * Operator function to get a specific child element by attribute value.
 *
 * @param attributeValue The value of the name attribute of the child element.
 * @return The child XMLEntity with the specified attribute value, or null if not found.
 */
operator fun XMLEntity.get(attributeValue: String): XMLEntity? {
    return gtChildren().find { it.getAttributes()["name"] == attributeValue }
}

/**
 * Operator function to navigate through the XML structure by element name.
 *
 * @param name The name of the element to find.
 * @return The child XMLEntity with the specified name, or null if not found.
 */
operator fun XMLEntity.div(name: String): XMLEntity? {
    return gtChildren().find { it.name == name }
}

/**
 * Infix function to iterate over the children of an XML element.
 *
 * @param action The action to perform on each child element.
 */
infix fun XMLEntity?.elements(action: (XMLEntity) -> Unit) {
    this?.gtChildren()?.forEach { action(it) }
}

/**
 * The main function demonstrating the creation and manipulation of an XML document.
 */
fun main() {
    // Create the XML structure
    val catalog = xml("catalog") {
        element("region") {
            attr("name", "lazio")
            element("vineyard") {
                attr("name", "frascati")
                element("wine") {
                    attr("name", "gotto d'oro")
                }
            }
            element("vineyard") {
                attr("name", "colonna")
                element("wine") {
                    attr("name", "fontana candida")
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

    // Access a specific region
    val region = catalog["lazio"]
    println("Region: ${region?.getAttributes()?.get("name")}")

    // Iterate over the elements of a region
    region elements {
        println("Element: ${it.getAttributes()?.get("name")}")
    }

    // Print the XML structure
    val printer = PrettyPrintVisitor()
    catalog.accept(printer)
    println(printer.getPrettyPrint())
}

