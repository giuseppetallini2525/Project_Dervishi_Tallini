
/**
 * Interface for visiting XML entities.
 * Implementers of this interface can define custom behaviors
 * for visiting and ending visits to XML entities.
 */
interface XMLEntityVisitor {

    /**
     * Visits an XML entity.
     *
     * @param entity The XML entity to visit.
     * @return A boolean indicating whether the visit was successful.
     */
    fun visit(entity: XMLEntity): Boolean

    /**
     * Ends the visit to an XML entity.
     *
     * @param entity The XML entity where the visit ends.
     */
    fun endVisit(entity: XMLEntity) {}
}
