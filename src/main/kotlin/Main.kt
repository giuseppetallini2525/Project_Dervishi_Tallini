import Catalog

fun main() {
    // Instantiate the root catalog
    val catalog = Catalog()

    // Instantiate the child region
    val region1 = Region("Region1", catalog)
    val region2 = Region("Region2", catalog)

    // Instantiate the sub-regions and vineyards
    val vineyard1 = Vineyard("Vineyard1", region1)
    val vineyard2 = Vineyard("Vineyard2", region1)
    val vineyard3 = Vineyard("Vineyard3", region2)

    // Instantiate the wines
    val wine1 = Wine("Wine1", "Red", vineyard1)
    val wine2 = Wine("Wine2", "White", vineyard1)
    val wine3 = Wine("Wine3", "Rose", vineyard2)
    val wine4 = Wine("Wine4", "Sparkling", vineyard3)

    // Set the elements property for the parent directories
    region1.addVineyard(vineyard1)
    region1.addVineyard(vineyard2)
    region2.addVineyard(vineyard3)

    vineyard1.addWine(wine1)
    vineyard1.addWine(wine2)
    vineyard2.addWine(wine3)
    vineyard3.addWine(wine4)

    catalog.addRegion(region1)
    catalog.addRegion(region2)

    // Print the structure
    println(catalog.toText())
}