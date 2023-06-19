package model

//clase de modelação de variáveis do tipo number presentes nos JSONs
data class JSONNumber(override val value: Number) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}