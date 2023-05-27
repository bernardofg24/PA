class SearchVisitor(val terms: List<String>, val flag: Boolean) : Visitor {
    val values = LinkedHashMap<String, MutableList<String>>()
    var key = ""

    init{
        if(!flag){
            terms.forEach {
                values.put(it, mutableListOf())
            }
        }
    }

    override fun visit(o: JSONObj){
        var hasProps = 0
        if(!flag) {
            if(key != ""){
                values[key]!!.add(o.toString())
                key = ""
            }
            o.props.forEach {
                if(it.key in terms){
                    key = it.key
                    it.value.accept(this)
                }else if(it.value is JSONObj || it.value is JSONCollection){
                    it.value.accept(this)
                }
            }
        }else{
            terms.forEach {
                if(it in o.props.keys){
                    hasProps++
                }
            }
            if(hasProps == terms.size){
                if(o::class.simpleName !in values.keys) {
                    values.put(o::class.simpleName!!, mutableListOf(o.toString()))
                }else{
                    values[o::class.simpleName]!!.add(o.toString())
                }
            }
            o.props.forEach {
                if(it.value is JSONObj || it.value is JSONCollection){
                    it.value.accept(this)
                }
            }
        }
    }

    override fun visit(c: JSONCollection){
        if(key != ""){
            values[key]!!.add(c.toString())
        }else if(c.value.first() is JSONObj){
            c.value.forEach {
                it.accept(this)
            }
        }
    }

    override fun visit(e: JSONElement){
        values[key]!!.add(e.toString())
    }
}