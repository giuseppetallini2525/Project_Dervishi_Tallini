/**
 * A visitor that removes a specific attribute from entities with a specific name in an XML document.
 *
 * @property targetEntityName The name of the entities whose attribute will be removed.
 * @property attributeName The name of the attribute to be removed.
 */
class RemoveAttributeVisitor(private val targetEntityName: String, private val attributeName: String) : XMLEntityVisitor {

    /**
     * Visits an XML entity and removes its attribute if the entity name matches the target entity name.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity): Boolean {
        if (entity.name == targetEntityName) {
            entity.getAttributes().remove(attributeName)  // Remove the attribute if the entity name matches
        }
        return true
    }
}
