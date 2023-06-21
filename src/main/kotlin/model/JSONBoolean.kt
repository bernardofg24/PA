package model

/*
Dado um boolean, a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/

data class JSONBoolean(override val value: Boolean) : JSONElement {

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return value.toString()
    }
}