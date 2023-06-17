package model

data class JSONChar(override val value: Char) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}