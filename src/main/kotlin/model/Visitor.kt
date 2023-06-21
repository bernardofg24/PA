package model

/*
Interface representativa da estrutura de um visitor
*/

interface Visitor {
    fun visit(o: JSONObj){}

    fun visit(c: JSONCollection){}

    fun visit(e: JSONElement){}
}