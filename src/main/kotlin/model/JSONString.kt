package model

data class JSONString(override val value: String) : JSONElement {
    override fun toString(): String {
        return "\"" + value + "\""
    }
}