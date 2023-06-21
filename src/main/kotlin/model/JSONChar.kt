package model

/*
Dado um caractér, a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/

data class JSONChar(override val value: Char) : JSONElement {

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return value.toString()
    }
}