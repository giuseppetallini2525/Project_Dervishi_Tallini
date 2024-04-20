import Vineyard

class Region:Element(val name: String, val parent: Catalog) {
    val vineyards: MutableList<Vineyard> = mutableListOf()

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            vineyards.forEach { it.accept(visitor) }
        }

    fun addVineyard(vineyard: Vineyard) {
        vineyards.add(vineyard)
    }


    fun deepElementCount(): Int {
        return 1 + vineyards.sumOf { it.deepElementCount() }
    }

    /**
    fun deepElementCount(): Int {
        return vineyards.sumOf { it.deepElementCount() } + 1
    }*/

    fun toText(): String {
        val sb = StringBuilder()
        sb.append("  - $name\n")
        for (vineyard in vineyards) {
            sb.append(vineyard.toText())
        }
        return sb.toString()
    }
}