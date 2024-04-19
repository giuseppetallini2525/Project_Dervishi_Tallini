class Wine(val name: String, val typeOfWine: String, val parent: Vineyard) {

    fun deepElementCount(): Int {
        return 1
    }

    fun toText(): String {
        return "      - $name ($typeOfWine)\n"
    }
}
