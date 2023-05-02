data class JSONString(val value: String) : JSONElement{
    override fun toString(): String {
        return "\"" + value + "\""
    }
}