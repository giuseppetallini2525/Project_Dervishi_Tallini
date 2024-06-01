
/**
 * A visitor that renames entities with a specific name in an XML document.
 *
 * @property oldName The current name of the entities to be renamed.
 * @property newName The new name to assign to the entities.
 */
class RenameEntityVisitor(private val oldName: String, private val newName: String) : XMLEntityVisitor {

    /**
     * Visits an XML entity and renames it if its name matches the old name.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity) : Boolean {
        if (entity.name == oldName) {
            entity.name = newName  // Change the entity's name
        }
        return true
    }
}
