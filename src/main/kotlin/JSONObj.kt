import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSuperclassOf

class JSONObj(obj: Any) : JSONElement{
    val props = LinkedHashMap<String, JSONElement>()

    private val types = listOf("String", "Boolean", "Char", "Array")

    init{
        require(obj::class.simpleName !in types && !Number::class.isSuperclassOf(obj::class) && !Collection::class.isSuperclassOf(obj::class)
                && !Map::class.isSuperclassOf(obj::class) && !Enum::class.isSuperclassOf(obj::class))
        val p = obj::class.declaredMemberProperties.associate { Pair(it.name, it.call(obj)) }
        p.entries.forEach {
            when(it.value){
                is String -> props.put(it.key, JSONString(it.value as String))
                is Boolean -> props.put(it.key, JSONBoolean(it.value as Boolean))
                is Char -> props.put(it.key, JSONChar(it.value as Char))
                is Array<*> -> props.put(it.key, JSONArray(it.value as Array<*>))
                else -> {
                    if(Number::class.isSuperclassOf(it.value!!::class)){
                        props.put(it.key, JSONNumber(it.value as Number))
                    }else if(Collection::class.isSuperclassOf(it.value!!::class)) {
                        props.put(it.key, JSONCollection(it.value as Collection<*>))
                    }else if(Map::class.isSuperclassOf(it.value!!::class)){
                        props.put(it.key, JSONMap(it.value as Map<*, *>))
                    }else if(Enum::class.isSuperclassOf(it.value!!::class)){
                        props.put(it.key, JSONEnum(it.value as Enum<*>))
                    }else{
                        props.put(it.key, JSONObj(it.value!!))
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("{")
        props.entries.forEach {
            if(props.keys.indexOf(it.key) != props.size - 1){
                str.append("\"" + it.key + "\": " + it.value.toString() + ", ")
            }else{
                str.append("\"" + it.key + "\": " + it.value.toString())
            }
        }
        return str.append("}").toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}