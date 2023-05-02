data class JSONCollection(val value: Collection<*>) : JSONElement{
    private fun <T> iterate(col: Iterable<T>): String {
        val str = StringBuilder().append("[")
        val iterator: Iterator<T> = col.iterator()
        while(iterator.hasNext()) {
            val next = iterator.next()
            if(next!!::class.simpleName == String::class.simpleName){
                if(!iterator.hasNext()){
                    str.append("\"" + next + "\"")
                }else{
                    str.append("\"" + next + "\", ")
                }
            }else{
                if(!iterator.hasNext()){
                    str.append(next.toString())
                }else{
                    str.append(next.toString() + ", ")
                }
            }
        }
        return str.append("]").toString()
    }

    override fun toString(): String {
        return iterate(value)
    }
}