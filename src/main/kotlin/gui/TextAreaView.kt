package gui

import model.JSONObj
import model.JSONObserver
import javax.swing.JTextArea

class TextAreaView(private val model: JSONObj) : JTextArea() {
    init{
        tabSize = 2
        text = model.toString()
        model.addObserver(object: JSONObserver{
            override fun propChanged() {
                text = model.toString()
            }
        })
    }
}