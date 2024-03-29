package model

/*
Visitor de validação que verifica se a propriedade passada como parâmetro obedece à estrutura do tipo esperado (tipo a comparar passado também como parâmetro)
*/

import kotlin.reflect.KClass

class ValidationVisitor(private val prop: String, private val clazz: KClass<*>) : Visitor {
    var bool = false
    private var key = ""
    private val valid = mutableListOf<Boolean>()

    override fun visit(o: JSONObj){
        if(key != ""){
            valid.add(o.obj::class == clazz)
            key = ""
        }
        o.value.forEach {
            if(it.key == prop){
                key = it.key
                it.value!!.accept(this)
            }else if(it.value is JSONObj || it.value is JSONCollection){
                it.value!!.accept(this)
            }
        }
        bool = !valid.contains(false)
    }

    override fun visit(c: JSONCollection){
        if(key != ""){
            valid.add(c.value.first().value::class == clazz)
        }else if(c.value.first() is JSONObj){
            c.value.forEach {
                it.accept(this)
            }
        }
    }

    override fun visit(e: JSONElement){
        valid.add(e.value::class == clazz)
    }
}