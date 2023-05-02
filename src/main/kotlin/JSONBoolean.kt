data class JSONBoolean(val value: Boolean) : JSONElement{
    override fun toString(): String {
        return value.toString()
    }
}