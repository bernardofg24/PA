interface JSONElement {
    override fun toString(): String

    fun accept(v: Visitor){
        v.visit(this)
    }
}