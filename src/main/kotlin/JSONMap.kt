data class JSONMap(val value: Map<*, *>) : JSONElement{
    override fun toString(): String {
        return value.toString()
    }
}