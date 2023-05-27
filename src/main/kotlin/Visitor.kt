interface Visitor {
    fun visit(o: JSONObj){}

    fun visit(c: JSONCollection){}

    fun visit(e: JSONElement){}
}