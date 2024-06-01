/**
 * A visitor that renames an attribute in entities with a specific name in an XML document.
 *
 * @property targetEntityName The name of the entities whose attributes will be renamed.
 * @property oldAttributeName The current name of the attribute to be renamed.
 * @property newAttributeName The new name to assign to the attribute.
 */
class RenameAttributeVisitor(
    private val targetEntityName: String,
    private val oldAttributeName: String,
    private val newAttributeName: String
) : XMLEntityVisitor {

    /**
     * Visits an XML entity and renames its attribute if its name matches the target entity name.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity): Boolean {
        if (entity.name == targetEntityName) {
            // Check if the old attribute name exists
            entity.getAttributes()[oldAttributeName]?.let { value ->
                // Remove the old attribute and add the new one with the same value
                entity.getAttributes().remove(oldAttributeName)
                entity.getAttributes()[newAttributeName] = value
            }
        }
        return true
    }
}
