import java.lang.StringBuilder
import kotlin.reflect.full.isSuperclassOf

class JSONMap(map: Map<*, *>) : JSONElement{
    val value = LinkedHashMap<String, JSONElement>()

    init{
        for(e in map.entries){
            when(e.value!!::class.simpleName){
                String::class.simpleName -> value.put(e.key.toString(), JSONString(e.value as String))
                Boolean::class.simpleName -> value.put(e.key.toString(), JSONBoolean(e.value as Boolean))
                Char::class.simpleName -> value.put(e.key.toString(), JSONChar(e.value as Char))
                Array::class.simpleName -> value.put(e.key.toString(), JSONArray(e.value as Array<*>))
                else -> {
                    if(Number::class.isSuperclassOf(e.value!!::class)){
                        value.put(e.key.toString(), JSONNumber(e.value as Number))
                    }else if(Collection::class.isSuperclassOf(e.value!!::class)){
                        value.put(e.key.toString(), JSONCollection(e.value as Collection<*>))
                    }else if(Map::class.isSuperclassOf(e.value!!::class)){
                        value.put(e.key.toString(), JSONMap(e.value as Map<*, *>))
                    }else if(Enum::class.isSuperclassOf(e.value!!::class)){
                        value.put(e.key.toString(), JSONEnum(e.value as Enum<*>))
                    }else{
                        value.put(e.key.toString(), JSONObj(e.value!!))
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("{")
        for(e in value.entries){
            if(value.keys.indexOf(e.key) != value.size - 1){
                str.append("\"" + e.key + "\": " + e.value.toString() + ", ")
            }else{
                str.append("\"" + e.key + "\": " + e.value.toString())
            }
        }
        return str.append("}").toString()
    }
}