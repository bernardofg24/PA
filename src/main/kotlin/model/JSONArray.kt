package model

import kotlin.reflect.full.isSuperclassOf

class JSONArray(arr: Array<*>) : JSONElement {
    override val value = arrayListOf<JSONElement>()

    init{
        if(arr.isArrayOf<String>()){
            arr.forEach { value.add(JSONString(it as String)) }
        }else if(arr.isArrayOf<Boolean>()){
            arr.forEach { value.add(JSONBoolean(it as Boolean)) }
        }else if(arr.isArrayOf<Char>()){
            arr.forEach { value.add(JSONChar(it as Char)) }
        }else if(arr.isArrayOf<Array<*>>()){
            arr.forEach { value.add(JSONArray(it as Array<*>)) }
        }else{
            if(Number::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONNumber(it as Number)) }
            }else if(Collection::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONCollection(it as Collection<*>)) }
            }else if(Map::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONMap(it as Map<*, *>)) }
            }else if(Enum::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONEnum(it as Enum<*>)) }
            }else{
                arr.forEach { value.add(JSONObj(it!!)) }
            }
        }
    }

    override fun toString(): String {
        val str = StringBuilder().append("[\n")
        var n = 0
        value.forEach {
            if(n == value.lastIndex){
                str.append(it.toString().prependIndent("\t"))
            }else{
                str.append((it.toString() + ",").prependIndent("\t") + "\n")
            }
            n++
        }
        return str.append("\n]").toString()
    }
}