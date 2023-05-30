package model

data class JSONBoolean(override val value: Boolean) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}