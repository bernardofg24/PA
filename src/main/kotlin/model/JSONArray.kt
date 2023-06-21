package model

import kotlin.reflect.full.isSuperclassOf

//clase de modelação de variáveis do tipo array presentes nos JSONs

class JSONArray(arr: Array<*>) : JSONElement {
    override val value = arrayListOf<JSONElement>()

    private val observers: MutableList<JSONObserver> = mutableListOf()

    init{
        if(arr.isArrayOf<String>()){
            arr.forEach { value.add(JSONString(it as String)) }
        }else if(arr.isArrayOf<Boolean>()){
            arr.forEach { value.add(JSONBoolean(it as Boolean)) }
        }else if(arr.isArrayOf<Char>()){
            arr.forEach { value.add(JSONChar(it as Char)) }
        }else{
            if(Number::class.isSuperclassOf(arr.first()!!::class)){
                arr.forEach { value.add(JSONNumber(it as Number)) }
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

    fun addObserver(observer: JSONObserver) = observers.add(observer)

    fun changeValue(index: Int, newValue: JSONElement){
        value[index] = newValue
        observers.forEach {
            it.update()
        }
    }

    fun addValue(newValue: JSONElement){
        value.add(newValue)
        observers.forEach {
            it.update()
        }
    }

    fun removeValue(oldValue: JSONElement){
        value.remove(oldValue)
        observers.forEach {
            it.update()
        }
    }
}