package model

import kotlin.reflect.full.isSuperclassOf

/*
Dado um array, a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/

class JSONArray(arr: Array<*>) : JSONElement {
    override val value = arrayListOf<JSONElement>()

    private val observers: MutableList<JSONObserver> = mutableListOf()

    /*
    Inicializa o elemento JSON a partir do array passado aquando da instanciação da classe
    */
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
            }
        }
    }

    /*
    Devolve o elemento JSON em formato de String
    */
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