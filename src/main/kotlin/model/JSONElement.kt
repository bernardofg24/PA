package model

/*
Interface representativa da estrutura base de um elemento JSON
*/

interface JSONElement {
    val value: Any

    override fun toString(): String

    fun accept(v: Visitor){
        v.visit(this)
    }
}