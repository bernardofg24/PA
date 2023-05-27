import kotlin.reflect.full.isSuperclassOf

class JSONMap(map: Map<*, *>) : JSONElement{
    val value = LinkedHashMap<String, JSONElement>()

    init{
        map.entries.forEach {
            when(it.value){
                is String -> value.put(it.key.toString(), JSONString(it.value as String))
                is Boolean -> value.put(it.key.toString(), JSONBoolean(it.value as Boolean))
                is Char -> value.put(it.key.toString(), JSONChar(it.value as Char))
                is Array<*> -> value.put(it.key.toString(), JSONArray(it.value as Array<*>))
                else -> {
                    if(Number::class.isSuperclassOf(it.value!!::class)){
                        value.put(it.key.toString(), JSONNumber(it.value as Number))
                    }else if(Collection::class.isSuperclassOf(it.value!!::class)){
                        value.put(it.key.toString(), JSONCollection(it.value as Collection<*>))
                    }else if(Map::class.isSuperclassOf(it.value!!::class)){
                        value.put(it.key.toString(), JSONMap(it.value as Map<*, *>))
                    }else if(Enum::class.isSuperclassOf(it.value!!::class)){
                        value.put(it.key.toString(), JSONEnum(it.value as Enum<*>))
                    }else{
                        value.put(it.key.toString(), JSONObj(it.value!!))
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("{")
        value.entries.forEach {
            if(value.keys.indexOf(it.key) != value.size - 1){
                str.append("\"" + it.key + "\": " + it.value.toString() + ", ")
            }else{
                str.append("\"" + it.key + "\": " + it.value.toString())
            }
        }
        return str.append("}").toString()
    }
}