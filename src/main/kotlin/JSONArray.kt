import java.lang.StringBuilder
import kotlin.reflect.full.isSuperclassOf

class JSONArray(arr: Array<*>) : JSONElement{
    val value = arrayListOf<JSONElement>()

    init{
        if(arr.isArrayOf<String>()){
            arr.forEach { value.add(JSONString(it as String)) }
        }else if(arr.isArrayOf<Boolean>()){
            arr.forEach { value.add(JSONBoolean(it as Boolean)) }
        }else if(arr.isArrayOf<Char>()){
            arr.forEach { value.add(JSONChar(it as Char)) }
        }else if(arr.isArrayOf<Array<*>>()){
            arr.forEach { value.add(JSONArray(it as Array<*>)) }
        }else if(arr.isArrayOf<Map<*, *>>()){
            arr.forEach { value.add(JSONMap(it as Map<*, *>)) }
        }else if(arr.isArrayOf<Enum<*>>()){
            arr.forEach { value.add(JSONEnum(it as Enum<*>)) }
        }else{
            if(Number::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONNumber(it as Number)) }
            }else if(Collection::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONCollection(it as Collection<*>)) }
            }else{
                arr.forEach { value.add(JSONObj(it!!)) }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("[")
        var n = 0
        for(v in value){
            if(n == value.lastIndex){
                str.append(v.toString())
            }else{
                str.append(v.toString() + ", ")
            }
            n++
        }
        return str.append("]").toString()
    }
}