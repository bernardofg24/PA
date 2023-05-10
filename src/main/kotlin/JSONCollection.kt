import java.lang.StringBuilder
import kotlin.reflect.full.isSuperclassOf

class JSONCollection(col: Collection<*>) : JSONElement{
    val value = mutableListOf<JSONElement>()

    init{
        iterateInit(col)
    }

    private fun <T> iterateInit(col: Iterable<T>){
        val iterator: Iterator<T> = col.iterator()
        while(iterator.hasNext()) {
            val next = iterator.next()
            when(next!!::class.simpleName){
                String::class.simpleName -> value.add(JSONString(next as String))
                Boolean::class.simpleName -> value.add(JSONBoolean(next as Boolean))
                Char::class.simpleName -> value.add(JSONChar(next as Char))
                Array::class.simpleName -> value.add(JSONArray(next as Array<*>))
                Map::class.simpleName -> value.add(JSONMap(next as Map<*, *>))
                Enum::class.simpleName -> value.add(JSONEnum(next as Enum<*>))
                else -> {
                    if(Number::class.isSuperclassOf(next!!::class)){
                        value.add(JSONNumber(next as Number))
                    }else if(Collection::class.isSuperclassOf(next!!::class)){
                        value.add(JSONCollection(next as Collection<*>))
                    }else{
                        value.add(JSONObj(next))
                    }
                }
            }
        }
    }

    private fun <T> iteratePrint(col: Iterable<T>): String{
        val iterator: Iterator<T> = col.iterator()
        val str = StringBuilder().append("[")
        while(iterator.hasNext()) {
            val next = iterator.next()
            if(!iterator.hasNext()){
                str.append(next.toString())
            }else{
                str.append(next.toString() + ", ")
            }
        }
        return str.append("]").toString()
    }

    override fun toString(): String {
        return iteratePrint(value)
    }
}