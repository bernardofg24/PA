data class JSONChar(val value: Char) : JSONElement{
    override fun toString(): String {
        return value.toString()
    }
}