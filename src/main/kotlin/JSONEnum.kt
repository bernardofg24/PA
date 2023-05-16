class JSONEnum(enum: Enum<*>) : JSONElement{
    val value = enum.name

    override fun toString(): String {
        return "\"" + value + "\""
    }
}