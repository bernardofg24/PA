package model

import kotlin.reflect.full.isSuperclassOf

/*
Dado um mapa, a classe faz o parse do mesmo para um objeto representativo de uma propriedade JSON
*/
class JSONMap(map: Map<*, *>) : JSONElement {
    override val value = LinkedHashMap<String, JSONElement>()

    private val observers: MutableList<JSONObserver> = mutableListOf()

    /*
    Inicializa o elemento JSON a partir do mapa passado aquando da instanciação da classe
    */
    init{
        map.entries.forEach {
            when(it.value){
                is String -> value[it.key.toString()] = JSONString(it.value as String)
                is Boolean -> value[it.key.toString()] = JSONBoolean(it.value as Boolean)
                is Char -> value[it.key.toString()] = JSONChar(it.value as Char)
                is Array<*> -> value[it.key.toString()] = JSONArray(it.value as Array<*>)
                else -> {
                    if(Number::class.isSuperclassOf(it.value!!::class)){
                        value[it.key.toString()] = JSONNumber(it.value as Number)
                    }else if(Collection::class.isSuperclassOf(it.value!!::class)){
                        value[it.key.toString()] = JSONCollection(it.value as Collection<*>)
                    }else if(Map::class.isSuperclassOf(it.value!!::class)){
                        value[it.key.toString()] = JSONMap(it.value as Map<*, *>)
                    }else if(Enum::class.isSuperclassOf(it.value!!::class)){
                        value[it.key.toString()] = JSONEnum(it.value as Enum<*>)
                    }else{
                        value[it.key.toString()] = JSONObj(it.value!!)
                    }
                }
            }
        }
    }

    /*
    Devolve o elemento JSON em formato de String
    */
    override fun toString(): String {
        val str = StringBuilder().append("{\n")
        value.entries.forEach {
            if(value.keys.indexOf(it.key) != value.size - 1){
                str.append(("\"" + it.key + "\": " + it.value.toString() + ",").prependIndent("\t") + "\n")
            }else{
                str.append(("\"" + it.key + "\": " + it.value.toString()).prependIndent("\t"))
            }
        }
        return str.append("\n}").toString()
    }

    /*
    Adiciona um observador sobre o elemento JSON
    */
    fun addObserver(observer: JSONObserver) = observers.add(observer)

    /*
    Passados a chave da entrada a alterar e o novo valor como parâmetros, efetua a substituição na estrutura do elemento JSON
    */
    fun changeEntry(key: String, newValue: JSONElement){
        value[key] = newValue
        observers.forEach {
            it.update()
        }
    }

    /*
    Passados a chave para a nova entrada e o seu valor como parâmetros, adiciona a mesma à estrutura do elemento JSON
    */
    fun addEntry(key: String, newValue: JSONElement){
        value.put(key, newValue)
        observers.forEach {
            it.update()
        }
    }

    /*
    Passada a chave da entrada a remover como parâmetro, remove a mesma da estrutura do elemento JSON
    */
    fun removeEntry(key: String){
        value.remove(key)
        observers.forEach {
            it.update()
        }
    }
}