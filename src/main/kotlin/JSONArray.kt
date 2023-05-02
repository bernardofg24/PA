import java.lang.StringBuilder

data class JSONArray(val value: Array<*>) : JSONElement{
    override fun toString(): String {
        val str = StringBuilder().append("[")
        if(value.isArrayOf<String>()){
            for(v in value){
                if(v == value.last()){
                    str.append("\"" + v + "\"")
                }else{
                    str.append("\"" + v + "\", ")
                }
            }
        }else{
            for(v in value){
                if(v == value.last()){
                    str.append(v.toString())
                }else{
                    str.append(v.toString() + ", ")
                }
            }
        }
        return str.append("]").toString()
    }
}