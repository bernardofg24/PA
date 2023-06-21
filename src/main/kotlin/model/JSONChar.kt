package model

//clase de modelação de variáveis do tipo char presentes nos JSONs

data class JSONChar(override val value: Char) : JSONElement {
    override fun toString(): String {
        return value.toString()
    }
}