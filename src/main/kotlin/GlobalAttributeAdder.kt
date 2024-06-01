/**
 * A visitor that adds or updates a specific attribute in entities with a specific name in an XML document.
 *
 * @property targetName The name of the entities to which the attribute will be added or updated.
 * @property attributeName The name of the attribute to add or update.
 * @property attributeValue The value of the attribute to add or update.
 */
class GlobalAttributeAdder(
    private val targetName: String,
    private val attributeName: String,
    private val attributeValue: String
) : XMLEntityVisitor {

    /**
     * Visits an XML entity and adds or updates its attribute if the entity name matches the target name.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity): Boolean {
        if (entity.name == targetName) {
            entity.setAttribute(attributeName, attributeValue)  // Set or update the attribute
        }
        return true
    }
}
