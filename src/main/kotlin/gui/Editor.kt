package gui

import model.*
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class Editor(model: JSONObj) {
    val frame = JFrame("JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(900, 900)

        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(editPanel(model)).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)

        val right = JPanel()
        right.layout = GridLayout()
        val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.text = model.toString()
        right.add(srcArea)
        add(right)
    }

    fun open() {
        frame.isVisible = true
    }

    fun editPanel(model: JSONObj): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            model.value.forEach {
                add(propWidget(it.key, it.value!!))
            }

            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")
                        add.addActionListener {
                            val text = JOptionPane.showInputDialog("text")
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        val del = JButton("delete all")
                        del.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        menu.add(add)
                        menu.add(del)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }


    fun propWidget(key: String = "", value: JSONElement): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            when(value){
                is JSONBoolean -> add(JCheckBox(key, value.value))
                is JSONCollection -> {
                    add(JLabel(key))
                    value.value.forEach {
                        if(it is JSONObj){
                            add(editPanel(it))
                        }else{
                            add(propWidget(value=it))
                        }
                    }
                }
                is JSONArray -> {
                    add(JLabel(key))
                    value.value.forEach {
                        add(propWidget(value=it))
                    }
                }
                is JSONMap -> {
                    add(JLabel(key))
                    value.value.forEach {
                        add(propWidget(it.key, it.value))
                    }
                }
                else -> {
                    add(JLabel(key))
                    add(JTextField(value.toString().replace("\"", "")))
                }
            }
            /*text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${text.text}")
                }
            })*/
        }
}