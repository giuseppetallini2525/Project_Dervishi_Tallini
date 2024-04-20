class Wine:Element(val name: String, val typeOfWine: String, val parent: Vineyard) {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    fun deepElementCount(): Int {
        return 1
    }

    fun toText(): String {
        return "      - $name ($typeOfWine)\n"
    }
}
