package model

/*
Dado um número(Int/Double/Float), a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/

data class JSONNumber(override val value: Number) : JSONElement {

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return value.toString()
    }
}