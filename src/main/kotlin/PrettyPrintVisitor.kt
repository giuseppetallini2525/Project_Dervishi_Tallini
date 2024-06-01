/**
 * A visitor that generates a pretty-printed string representation of an XML document.
 */
class PrettyPrintVisitor : XMLEntityVisitor {
    private val sb = StringBuilder()
    var queryMode = false
    private var baseDepth = 0

    /**
     * Gets the pretty-printed XML as a string.
     *
     * @return The pretty-printed XML string.
     */
    fun getPrettyPrint(): String = sb.toString().trim()

    /**
     * Visits an XML entity and generates its pretty-printed representation.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity): Boolean {
        if (queryMode) {
            printQuery(entity)
        } else {
            val indent = "  ".repeat(entity.depth)
            sb.append("$indent<${entity.name}${formatAttributes(entity.getAttributes())}")

            if (entity.textContent.isNullOrEmpty() && entity.gtChildren().isEmpty()) {
                sb.append("/>\n")
            } else {
                sb.append(">")
                if (!entity.textContent.isNullOrEmpty()) {
                    sb.append(entity.textContent)
                }
                if (entity.gtChildren().isNotEmpty()) {
                    sb.append("\n")
                }
            }
        }
        return true
    }

    /**
     * Ends the visit to an XML entity, closing its tag in the pretty-printed representation.
     *
     * @param entity The XML entity where the visit ends.
     */
    override fun endVisit(entity: XMLEntity) {
        if (queryMode) {
            endQuery(entity)
        } else {
            if (!entity.textContent.isNullOrEmpty()) {
                sb.append("</${entity.name}>\n")
            } else if (entity.gtChildren().isNotEmpty()) {
                val indent = "  ".repeat(entity.depth)
                sb.append("$indent</${entity.name}>\n")
            }
        }
    }

    /**
     * Formats the attributes of an XML entity into a string.
     *
     * @param attributes The map of attributes to format.
     * @return The formatted string of attributes.
     */
    private fun formatAttributes(attributes: Map<String, String>): String {
        return if (attributes.isNotEmpty()) {
            attributes.entries.joinToString(" ", prefix = " ") { (key, value) -> "$key=\"$value\"" }
        } else {
            ""
        }
    }

    /**
     * Prints the query result for an XML entity.
     *
     * @param entity The XML entity to print.
     */
    private fun printQuery(entity: XMLEntity) {
        if (baseDepth == 0) {
            baseDepth = entity.depth
        }
        val indent = "  ".repeat(entity.depth - baseDepth)
        sb.append("$indent<${entity.name}${formatAttributes(entity.getAttributes())}")

        if (entity.textContent.isNullOrEmpty() && entity.gtChildren().isEmpty()) {
            sb.append(" />\n")
        } else {
            sb.append(">")
            if (!entity.textContent.isNullOrEmpty()) {
                sb.append(entity.textContent)
            }
            if (entity.gtChildren().isNotEmpty()) {
                sb.append("\n")
            }
        }
    }

    /**
     * Ends the query result for an XML entity, closing its tag in the pretty-printed representation.
     *
     * @param entity The XML entity where the query ends.
     */
    private fun endQuery(entity: XMLEntity) {
        val indent = "  ".repeat(entity.depth - baseDepth)
        if (entity.gtChildren().isNotEmpty()) {
            sb.append("$indent</${entity.name}>\n")
        } else if (!entity.textContent.isNullOrEmpty()) {
            sb.append("</${entity.name}>\n")
        }
    }

    /**
     * Prints the query result for a list of XML entities.
     *
     * @param components The list of XML entities to print.
     */
    fun printQueryResult(components: List<XMLEntity>) {
        if (components.isNotEmpty()) {
            baseDepth = components.first().depth
            components.forEach { it.accept(this) }
        }
    }
}
