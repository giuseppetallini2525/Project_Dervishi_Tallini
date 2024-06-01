
/**
 * The main function demonstrating the creation and manipulation of an XML document.
 */
fun main() {

    // Create a region entity with nested vineyards and wines
    val region = XMLEntity("region").apply {
        setAttribute("name", "Lazio")
        addChild(XMLEntity("vineyard").apply {
            setAttribute("name", "Fontana Candida")
            addChild(XMLEntity("wine").apply { textContent = "Frascati Superiore" })
            addChild(XMLEntity("wine").apply { textContent = "Santa Teresa" })
        })
        addChild(XMLEntity("vineyard").apply {
            setAttribute("name", "Gotto d'oro")
            addChild(XMLEntity("wine").apply { textContent = "Marino Bianco" })
            addChild(XMLEntity("wine").apply { textContent = "Marino Superiore" })
        })
        // Add more vineyards and wines as needed
    }

    // Create an author entity
    val author = XMLEntity("author").apply { textContent = "Giuseppe Tallini and Enada Dervishi" }

    // Create the catalog entity and add the author and region entities
    val catalog = XMLEntity("catalog").apply {
        addChild(author)
        addChild(region)
    }

    // Initialize a PrettyPrintVisitor for output
    val printer = PrettyPrintVisitor()
    // Use PrettyPrintVisitor to print and check the output
    catalog.accept(printer)
    println(printer.getPrettyPrint())

    // Apply the GlobalAttributeAdder visitor to add an attribute to all wine entities
    val adder = GlobalAttributeAdder("wine", "alcohol", "12")
    catalog.accept(adder)

    // Apply the RenameEntityVisitor to rename wine entities to ItalianWines
    val renameVisitor = RenameEntityVisitor("wine", "ItalianWines")
    catalog.accept(renameVisitor)

    // Apply the RenameAttributeVisitor to rename the region's name attribute to realName
    val renameAttVisitor = RenameAttributeVisitor("region", "name", "realName")
    catalog.accept(renameAttVisitor)

    // Apply the RemoveEntityVisitor to remove ItalianWines entities
    val visitor = RemoveEntityVisitor("ItalianWines")
    catalog.accept(visitor)

    // Apply the RemoveAttributeVisitor to remove the name attribute from vineyard entities
    val removeAttributeVisitor = RemoveAttributeVisitor("vineyard", "name")
    catalog.accept(removeAttributeVisitor)


    // Create an XML Document whose root is the catalog
    val xmlDoc = XMLDoc(catalog)

    // Initialize a PrettyPrintVisitor for query results
    val queryPrinter = PrettyPrintVisitor()
    queryPrinter.queryMode = true

    // Query the document with a simple path and print the results
    val components = catalog.query("catalog/region")
    queryPrinter.printQueryResult(components)
    println(queryPrinter.getPrettyPrint())
}
