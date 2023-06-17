package model

interface JSONElement {
    val value: Any

    override fun toString(): String

    fun accept(v: Visitor){
        v.visit(this)
    }
}