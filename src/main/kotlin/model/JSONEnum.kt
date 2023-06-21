package model

/*
Dado um enum, a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/

class JSONEnum(enum: Enum<*>) : JSONElement {
    override val value = enum.name

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return "\"" + value + "\""
    }
}