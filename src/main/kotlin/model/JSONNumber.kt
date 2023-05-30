package model

data class JSONNumber(override val value: Number) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}