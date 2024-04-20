import Wine

class Vineyard:Element(val name: String, val parent: Region) {
    val wines: MutableList<Wine> = mutableListOf()

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            wines.forEach { it.accept(visitor) }
        }

    fun addWine(wine: Wine) {
        wines.add(wine)
    }


    fun deepElementCount(): Int {
        return 1 + wines.size
    }

    /**
    fun deepElementCount(): Int {
        return wines.sumOf { it.deepElementCount() } + 1
    }*/

    fun toText(): String {
        val sb = StringBuilder()
        sb.append("    - $name\n")
        for (wine in wines) {
            sb.append(wine.toText())
        }
        return sb.toString()
    }
}