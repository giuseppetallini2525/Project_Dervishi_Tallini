interface Visitor {
    fun visit(catalogue: Catalog): Boolean
    fun visit(region: Region): Boolean
    fun visit(vineyard: Vineyard): Boolean
    fun visit(wine: Wine): Boolean
}
