/**
 * A visitor that removes entities with a specific name from an XML document.
 *
 * @property targetName The name of the entities to be removed.
 */
class RemoveEntityVisitor(private val targetName: String) : XMLEntityVisitor {

    /**
     * Visits an XML entity and removes its child entities if their name matches the target name.
     *
     * @param entity The XML entity to visit.
     * @return Always returns true to continue visiting other entities.
     */
    override fun visit(entity: XMLEntity): Boolean {
        // Using an iterator to safely modify the collection during iteration
        val iterator = entity.gtChildren().iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child.name == targetName) {
                iterator.remove() // Correctly use iterator's remove method to delete the current element
            } else {
                visit(child)  // Recursively visit the child if it does not match the targetName
            }
        }
        return true
    }
}
