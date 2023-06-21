package model

//clase de modelação de variáveis do tipo string presentes nos JSONs

data class JSONString(override val value: String) : JSONElement {
    override fun toString(): String {
        return "\"" + value + "\""
    }
}