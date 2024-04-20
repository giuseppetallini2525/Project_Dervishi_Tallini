import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CatalogTest {
    val catalog = Catalog()
    val region1 = Region("Region1", catalog)
    val region2 = Region("Region2", catalog)
    val vineyard1 = Vineyard("Vineyard1", region1)
    val vineyard2 = Vineyard("Vineyard2", region1)
    val vineyard3 = Vineyard("Vineyard3", region2)
    val wine1 = Wine("Wine1", "Red", vineyard1)
    val wine2 = Wine("Wine2", "White", vineyard1)
    val wine3 = Wine("Wine3", "Rose", vineyard2)
    val wine4 = Wine("Wine4", "Sparkling", vineyard3)

    init {
        region1.addVineyard(vineyard1)
        region1.addVineyard(vineyard2)
        region2.addVineyard(vineyard3)

        vineyard1.addWine(wine1)
        vineyard1.addWine(wine2)
        vineyard2.addWine(wine3)
        vineyard3.addWine(wine4)

        catalog.addRegion(region1)
        catalog.addRegion(region2)
    }


    @Test
    fun testDeepElementCount() {
        assertEquals(9, catalog.deepElementCount())
        assertEquals(5, region1.deepElementCount())
        assertEquals(3, vineyard1.deepElementCount())
        assertEquals(2, wine1.deepElementCount())
    }

    @Test
    fun testDepth() {
        assertEquals(0, catalog.depth())
        assertEquals(1, region1.depth())
        assertEquals(2, vineyard1.depth())
        assertEquals(3, wine1.depth())
    }
}