class WineCatalogVisitor : Visitor {

    override fun visit(catalog: Catalog): Boolean {
        println("Catalog: ${catalog.name}, Depth: ${catalog.depth}")
        catalog.regions.forEach { it.accept(this) }
        return true
    }

    override fun visit(region: Region): Boolean {
        println("Region: ${region.name}, Depth: ${region.depth}")
        region.vineyards.forEach { it.accept(this) }
        return true
    }

    override fun visit(vineyard: Vineyard): Boolean {
        println("Vineyard: ${vineyard.name}, Depth: ${vineyard.depth}")
        vineyard.wines.forEach { it.accept(this) }
        return true
    }

    override fun visit(wine: Wine): Boolean {
        println("Wine: ${wine.name} (${wine.typeOfWine}), Depth: ${wine.depth}")
        // Wine is a leaf node; no need to change the depth
        return true
    }
}