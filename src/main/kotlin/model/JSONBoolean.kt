package model

//clase de modelação de variáveis do tipo boolean presentes nos JSONs

data class JSONBoolean(override val value: Boolean) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}