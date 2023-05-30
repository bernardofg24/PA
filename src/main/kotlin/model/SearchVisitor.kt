package model

class SearchVisitor(vararg term: String, private val flag: Boolean) : Visitor {
    private val terms = term.toList()
    val values = LinkedHashMap<String, MutableList<Any>>()
    private var key = ""

    init{
        if(!flag){
            terms.forEach {
                values[it] = mutableListOf()
            }
        }
    }

    override fun visit(o: JSONObj){
        var hasProps = 0
        if(!flag) {
            if(key != ""){
                values[key]!!.add(o.value)
                key = ""
            }
            o.value.forEach {
                if(it.key in terms){
                    key = it.key
                    it.value!!.accept(this)
                }else if(it.value is JSONObj || it.value is JSONCollection){
                    it.value!!.accept(this)
                }
            }
        }else{
            terms.forEach {
                if(it in o.value.keys){
                    hasProps++
                }
            }
            if(hasProps == terms.size){
                if(o::class.simpleName !in values.keys) {
                    values[o::class.simpleName!!] = mutableListOf(o)
                }else{
                    values[o::class.simpleName]!!.add(o)
                }
            }
            o.value.forEach {
                if(it.value is JSONObj || it.value is JSONCollection){
                    it.value!!.accept(this)
                }
            }
        }
    }

    override fun visit(c: JSONCollection){
        if(key != ""){
            values[key]!!.add(c.value)
        }else if(c.value.first() is JSONObj){
            c.value.forEach {
                it.accept(this)
            }
        }
    }

    override fun visit(e: JSONElement){
        values[key]!!.add(e.value)
    }
}