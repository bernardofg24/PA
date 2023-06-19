package model

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSuperclassOf

class JSONObj(val obj: Any) : JSONElement {
    override val value = LinkedHashMap<String, JSONElement?>()
    private val types = listOf(String::class, Boolean::class, Char::class, Array::class)

    private val observers: MutableList<JSONObserver> = mutableListOf()

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
        val str = StringBuilder().append("{\n")
        value.entries.forEach {
            if(value.keys.indexOf(it.key) != value.size - 1){
                if(it.value == null){
                    str.append(("\"" + it.key + "\" : " + null + ",").prependIndent("\t") + "\n")
                }else {
                    str.append(("\"" + it.key + "\" : " + it.value.toString() + ",").prependIndent("\t") + "\n")
                }
            }else{
                if(it.value == null){
                    str.append(("\"" + it.key + "\" : " + null).prependIndent("\t"))
                }else{
                    str.append(("\"" + it.key + "\" : " + it.value.toString()).prependIndent("\t"))
                }
            }
        }
        return str.append("\n}").toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }

    fun addObserver(observer: JSONObserver) = observers.add(observer)

    fun changeProp(prop: String, newValue: Any){
        when(newValue){
            is String -> value[prop] = JSONString(newValue)
            is Boolean -> value[prop] = JSONBoolean(newValue)
            is Char -> value[prop] = JSONChar(newValue)
            is Array<*> -> value[prop] = JSONArray(newValue)
            else -> {
                if(Number::class.isSuperclassOf(newValue::class)){
                    value[prop] = JSONNumber(newValue as Number)
                }else if(Collection::class.isSuperclassOf(newValue::class)) {
                    value[prop] = JSONCollection(newValue as Collection<*>)
                }else if(Map::class.isSuperclassOf(newValue::class)){
                    value[prop] = JSONMap(newValue as Map<*, *>)
                }else if(Enum::class.isSuperclassOf(newValue::class)){
                    value[prop] = JSONEnum(newValue as Enum<*>)
                }else{
                    value[prop] = JSONObj(newValue)
                }
            }
        }
        observers.forEach {
            it.propChanged()
        }
    }
}