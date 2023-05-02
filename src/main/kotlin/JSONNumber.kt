data class JSONNumber(val value: Number) : JSONElement{
    override fun toString(): String {
        return value.toString()
    }
}