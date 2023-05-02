data class JSONEnum(val value: Enum<*>) : JSONElement{
    override fun toString(): String {
        return value.toString()
    }
}