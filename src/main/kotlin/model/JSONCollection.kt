package model

import kotlin.reflect.full.isSuperclassOf

/*
Dada uma lista, a classe faz o parse da mesma para um objeto representativo de uma propriedade JSON
*/

class JSONCollection(col: Collection<*>) : JSONElement {
    override val value = mutableListOf<JSONElement>()

    private val observers: MutableList<JSONObserver> = mutableListOf()

    /*
    Inicializa o elemento JSON a partir da lista passada aquando da instanciação da classe
    */
    init{
        iterateInit(col)
    }

    /*
    Iterador auxiliar à inicialização
    */
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

    /*
    Iterador auxiliar à escrita do elemento JSON em String
    */
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

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        return iteratePrint(value)
    }

    /*
    Função auxiliar aos Visitors
    */
    override fun accept(v: Visitor) {
        v.visit(this)
    }

    /*
    Adiciona um observador sobre o elemento JSON
    */
    fun addObserver(observer: JSONObserver) = observers.add(observer)

    /*
    Passados o índice da entrada a alterar e o novo valor como parâmetros, efetua a substituição na estrutura do elemento JSON
    */
    fun changeValue(index: Int, newValue: JSONElement){
        value[index] = newValue
        observers.forEach {
            it.update()
        }
    }

    /*
    Passado o valor a adicionar como parâmetro, adiciona o mesmo à estrutura do elemento JSON
    */
    fun addValue(newValue: JSONElement){
        value.add(newValue)
        observers.forEach {
            it.update()
        }
    }

    /*
    Passado o valor a remover como parâmetro, remove o mesmo da estrutura do elemento JSON
    */
    fun removeValue(oldValue: JSONElement){
        value.remove(oldValue)
        observers.forEach {
            it.update()
        }
    }
}