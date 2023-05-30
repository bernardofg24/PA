import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSuperclassOf

class JSONObj(val obj: Any) : JSONElement{
    override val value = LinkedHashMap<String, JSONElement?>()
    private val types = listOf(String::class, Boolean::class, Char::class, Array::class)

    init{
        require(obj::class !in types && !Number::class.isSuperclassOf(obj::class) && !Collection::class.isSuperclassOf(obj::class)
                && !Map::class.isSuperclassOf(obj::class) && !Enum::class.isSuperclassOf(obj::class))
        val p = obj::class.declaredMemberProperties.associateBy { it.name }.toMutableMap()
        p.entries.forEach {
            if(it.value.hasAnnotation<DoNotInitiate>()){
                p.remove(it.key)
            }
        }
        p.entries.forEach {
            var inst = it.value.call(obj)
            if(it.value.hasAnnotation<ToString>()){
                inst = it.value.call(obj).toString()
            }
            when(inst){
                is String -> value[it.key] = JSONString(inst)
                is Boolean -> value[it.key] = JSONBoolean(inst)
                is Char -> value[it.key] = JSONChar(inst)
                is Array<*> -> value[it.key] = JSONArray(inst)
                else -> {
                    if(inst == null){
                        value[it.key] = inst
                    }else if(Number::class.isSuperclassOf(inst::class)){
                        value[it.key] = JSONNumber(inst as Number)
                    }else if(Collection::class.isSuperclassOf(inst::class)) {
                        value[it.key] = JSONCollection(inst as Collection<*>)
                    }else if(Map::class.isSuperclassOf(inst::class)){
                        value[it.key] = JSONMap(inst as Map<*, *>)
                    }else if(Enum::class.isSuperclassOf(inst::class)){
                        value[it.key] = JSONEnum(inst as Enum<*>)
                    }else{
                        value[it.key] = JSONObj(inst)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("{")
        value.entries.forEach {
            if(value.keys.indexOf(it.key) != value.size - 1){
                if(it.value == null){
                    str.append("\"" + it.key + "\": " + null + ", ")
                }else {
                    str.append("\"" + it.key + "\": " + it.value.toString() + ", ")
                }
            }else{
                if(it.value == null){
                    str.append("\"" + it.key + "\": " + null)
                }else{
                    str.append("\"" + it.key + "\": " + it.value.toString())
                }
            }
        }
        return str.append("}").toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}