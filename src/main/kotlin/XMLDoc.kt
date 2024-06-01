import java.io.File

/**
 * Represents an XML Document.
 *
 * @property root The root entity of the XML document.
 */
class XMLDoc(val root: XMLEntity) {

    /**
     * Writes the document to a file using a visitor for formatting.
     *
     * @param filename The name of the file to write to.
     * @param visitor The visitor used to format the XML content.
     */
    fun writeToFile(filename: String, visitor: XMLEntityVisitor) {
        root.accept(visitor)  // Apply the visitor to format the XML
        val content = if (visitor is PrettyPrintVisitor) {
            visitor.getPrettyPrint()  // Assuming visitor can return the formatted string
        } else {
            ""  // If the visitor is not a PrettyPrintVisitor, handle accordingly or throw an error
        }
        File(filename).writeText(content)
    }
}




