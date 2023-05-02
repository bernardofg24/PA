import java.lang.StringBuilder
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSuperclassOf

class JSONObj(obj: Any) : JSONElement{
    val props = LinkedHashMap<String, JSONElement>()

    private val types = listOf("Number", "String", "Boolean", "Char", "Array", "Collection", "Map", "Enum")

    init{
        require(obj::class.simpleName !in types)
        val p = obj::class.declaredMemberProperties.associate { Pair(it.name, it.call(obj)) }
        for(e in p.entries){
            when(e.value!!::class.simpleName){
                String::class.simpleName -> props.put(e.key, JSONString(e.value as String))
                Boolean::class.simpleName -> props.put(e.key, JSONBoolean(e.value as Boolean))
                Char::class.simpleName -> props.put(e.key, JSONChar(e.value as Char))
                Array::class.simpleName -> props.put(e.key, JSONArray(e.value as Array<*>))
                Collection::class.simpleName -> props.put(e.key, JSONCollection(e.value as Collection<*>))
                Map::class.simpleName -> props.put(e.key, JSONMap(e.value as Map<*, *>))
                Enum::class.simpleName -> props.put(e.key, JSONEnum(e.value as Enum<*>))
                else -> {
                    if(Number::class.isSuperclassOf(e.value!!::class)){
                        props.put(e.key, JSONNumber(e.value as Number))
                    }else{
                        props.put(e.key, JSONObj(e.value!!))
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder()
        for(e in props.entries){
            str.append("\"" + e.key + "\": " + e.value.toString() + "\n")
        }
        return str.toString()
    }
}