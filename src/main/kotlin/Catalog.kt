class Catalog : Element {
    val name: String = "Catalogue"
    val regions: MutableList<Region> = mutableListOf()
    override var depth: Int = 0

    // ... other methods ...

   /* override fun accept(visitor: Visitor): Boolean {
        if (visitor.visit(this)) {
            regions.forEach { it.accept(visitor) }
        }
        return true
    }*/
   override fun accept(visitor: Visitor): Boolean = visitor.visit(this)

    fun addRegion(region: Region) {
        regions.add(region)
    }

    fun deepElementCount(): Int = 1 + regions.sumOf { it.deepElementCount() }

    fun toText(): String {
        val sb = StringBuilder()
        sb.append(name).append("\n")
        for (region in regions) {
            sb.append(region.toText())
        }
        return sb.toString()
    }

}

class Region(val name: String, val parent: Catalog) : Element {
    val vineyards: MutableList<Vineyard> = mutableListOf()
    override var depth: Int = parent.depth + 1

    // ... other methods ...

    /*override fun accept(visitor: Visitor): Boolean {
        if (visitor.visit(this)) {
            vineyards.forEach { it.accept(visitor) }
        }
        return true
    }*/
    override fun accept(visitor: Visitor): Boolean = visitor.visit(this)

    fun addVineyard(vineyard: Vineyard) {
        vineyards.add(vineyard)
    }
    fun deepElementCount(): Int = 1 + vineyards.sumOf { it.deepElementCount() }

    fun toText(): String {
        val sb = StringBuilder()
        sb.append("  - $name\n")
        for (vineyard in vineyards) {
            sb.append(vineyard.toText())
        }
        return sb.toString()
    }
}

class Vineyard(val name: String, val parent: Region) : Element {
    val wines: MutableList<Wine> = mutableListOf()
    override var depth: Int = parent.depth + 1

    // ... other methods ...

    /*override fun accept(visitor: Visitor): Boolean {
        if (visitor.visit(this)) {
            wines.forEach { it.accept(visitor) }
        }
        return true
    }*/
    override fun accept(visitor: Visitor): Boolean = visitor.visit(this)

    fun addWine(wine: Wine) {
        wines.add(wine)
    }
    fun deepElementCount(): Int = 1 + wines.sumOf { it.deepElementCount() }

    fun toText(): String {
        val sb = StringBuilder()
        sb.append("    - $name\n")
        for (wine in wines) {
            sb.append(wine.toText())
        }
        return sb.toString()
    }
}

class Wine(val name: String, val typeOfWine: String, val parent: Vineyard) : Element {
    override var depth: Int = parent.depth + 1

    // ... other methods ...

    override fun accept(visitor: Visitor): Boolean {
        return visitor.visit(this)
    }
    fun toText(): String {
        return "      - $name ($typeOfWine)\n"
    }

    fun deepElementCount(): Int = 1 // Wine has no further sub-elements



}
