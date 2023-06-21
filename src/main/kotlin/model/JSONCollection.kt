package model

import kotlin.reflect.full.isSuperclassOf

//clase de modelação de variáveis pertencentes à propriedade collection presentes nos JSONs

class JSONCollection(col: Collection<*>) : JSONElement {
    override val value = mutableListOf<JSONElement>()

    private val observers: MutableList<JSONObserver> = mutableListOf()

    init{
        iterateInit(col)
    }

    private fun <T> iterateInit(col: Iterable<T>){
        val iterator: Iterator<T> = col.iterator()
        while(iterator.hasNext()) {
            val next = iterator.next()
            when(next){
                is String -> value.add(JSONString(next as String))
                is Boolean -> value.add(JSONBoolean(next as Boolean))
                is Char -> value.add(JSONChar(next as Char))
                is Array<*> -> value.add(JSONArray(next as Array<*>))
                else -> {
                    if(Number::class.isSuperclassOf(next!!::class)){
                        value.add(JSONNumber(next as Number))
                    }else if(Collection::class.isSuperclassOf(next!!::class)){
                        value.add(JSONCollection(next as Collection<*>))
                    }else if(Map::class.isSuperclassOf(next!!::class)){
                        value.add(JSONMap(next as Map<*, *>))
                    }else if(Enum::class.isSuperclassOf(next!!::class)){
                        value.add(JSONEnum(next as Enum<*>))
                    }else{
                        value.add(JSONObj(next))
                    }
                }
            }
        }
    }

    private fun <T> iteratePrint(col: Iterable<T>): String{
        val iterator: Iterator<T> = col.iterator()
        val str = StringBuilder().append("[\n")
        while(iterator.hasNext()) {
            val next = iterator.next()
            if(!iterator.hasNext()){
                str.append(next.toString().prependIndent("\t"))
            }else{
                str.append((next.toString() + ",").prependIndent("\t") + "\n")
            }
        }
        return str.append("\n]").toString()
    }

    override fun toString(): String {
        return iteratePrint(value)
    }

    override fun accept(v: Visitor) {
        v.visit(this)
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