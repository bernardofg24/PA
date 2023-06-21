package model

/*
Dada uma string, a classe faz o parse da mesma para um objeto representativo de uma propriedade JSON
*/

data class JSONString(override val value: String) : JSONElement {

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return "\"" + value + "\""
    }
}