class JSONEnum(enum: Enum<*>) : JSONElement{
    override val value = enum.name

    override fun toString(): String {
        return "\"" + value + "\""
    }
}