package model

//clase de modelação de variáveis do tipo enum presentes nos JSONs

class JSONEnum(enum: Enum<*>) : JSONElement {
    override val value = enum.name

    override fun toString(): String {
        return "\"" + value + "\""
    }
}