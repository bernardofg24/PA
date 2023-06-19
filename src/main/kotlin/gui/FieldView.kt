package gui

import model.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JTextField
import kotlin.reflect.full.isSuperclassOf

class FieldView(private val model: JSONObj, prop: String) : JTextField() {
    private val observers: MutableList<FieldObserver> = mutableListOf()

    fun addObserver(observer: FieldObserver) = observers.add(observer)

    init{
        text = model.value[prop].toString().replace("\"", "")

        model.addObserver(object : JSONObserver{
            override fun propChanged() {
                text = model.value[prop].toString().replace("\"", "")
            }
        })

        addActionListener(object : ActionListener{
            override fun actionPerformed(e: ActionEvent?) {
                observers.forEach {
                    when(model.value[prop]){
                        is JSONString -> it.changeValue(prop, text)
                        is JSONBoolean -> it.changeValue(prop, text.toBoolean())
                        is JSONNumber -> it.changeValue(prop, text.toDouble())
                    }
                }
            }
        })
    }
}