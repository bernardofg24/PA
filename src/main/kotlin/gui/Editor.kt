package gui

import model.*
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import kotlin.math.E

class Editor(val model: JSONObj) {
    val frame = frame()
    var new = true
    val left = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JSONEditor())
        val undo = JButton("Undo")
        undo.alignmentX = Component.CENTER_ALIGNMENT
        undo.addActionListener {
            if(cmds.isNotEmpty()){
                cmds.last().undo()
                cmds.removeLast()
            }
        }
        add(undo)
    }
    val right = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JSONText())
    }
    val cmds = mutableListOf<Command>()

    fun frame(): JFrame =
        JFrame("JSON Object Editor").apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            layout = GridLayout(0, 2)
            size = Dimension(900, 900)
        }

    fun JSONText(): JTextArea =
        JTextArea().apply {
            tabSize = 2
            isEditable = false
            text = model.toString()
        }

    fun JSONEditor(): JScrollPane =
        JScrollPane(objPanel(model)).apply {
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }

    fun open() {
        frame.add(left)
        frame.add(right)
        new = false
        frame.isVisible = true
    }

    fun objPanel(elem: JSONObj): JPanel =
        JPanel().apply {
            layout = GridLayout(0,2)
            val line = BorderFactory.createMatteBorder(5,5,5,5, Color.BLACK)
            border = line

            if(new) {
                elem.addObserver(object : JSONObserver {
                    override fun update() {
                        left.removeAll()
                        left.add(JSONEditor())
                        val undo = JButton("Undo")
                        undo.alignmentX = Component.CENTER_ALIGNMENT
                        undo.addActionListener {
                            if(cmds.isNotEmpty()){
                                cmds.last().undo()
                                cmds.removeLast()
                            }
                        }
                        left.add(undo)
                        left.revalidate()
                        right.removeAll()
                        right.add(JSONText())
                        right.revalidate()
                    }
                })
            }

            elem.value.forEach {
                add(JLabel(it.key))
                val p = it.value
                when(p){
                    is JSONObj -> {
                        add(objPanel(p))
                    }
                    is JSONBoolean -> {
                        val panel =
                            JPanel().apply {
                                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                                alignmentX = Component.LEFT_ALIGNMENT
                                alignmentY = Component.TOP_ALIGNMENT

                                val field = JCheckBox("", p.value)
                                val bool = field.isSelected
                                field.addFocusListener(object : FocusAdapter() {
                                    override fun focusLost(e: FocusEvent) {
                                        if(field.isSelected != bool){
                                            cmds.add(object : Command {
                                                override fun run() {
                                                    elem.changeProp(it.key, JSONBoolean(field.isSelected))
                                                }

                                                override fun undo() {
                                                    elem.changeProp(it.key, JSONBoolean(bool))
                                                }
                                            })
                                            cmds.last().run()
                                        }
                                    }
                                })
                                add(field)
                            }

                        add(panel)
                    }
                    is JSONMap -> {
                        add(mapPanel(p))
                    }
                    else -> {
                        if(p is JSONArray || p is JSONCollection){
                            add(listPanel(p))
                        }else{
                            val panel =
                                JPanel().apply {
                                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                                    alignmentX = Component.LEFT_ALIGNMENT
                                    alignmentY = Component.TOP_ALIGNMENT

                                    if(p is JSONEnum){
                                        add(JLabel(p.toString().replace("\"", "")))
                                    }else {
                                        val field = JTextField(p.toString().replace("\"", ""))
                                        val text = field.text
                                        field.addFocusListener(object : FocusAdapter() {
                                            override fun focusLost(e: FocusEvent) {
                                                if (field.text != text) {
                                                    cmds.add(object : Command {
                                                        override fun run() {
                                                            if (field.text.toIntOrNull() != null) {
                                                                elem.changeProp(it.key, JSONNumber(field.text.toInt()))
                                                            } else if (field.text.toDoubleOrNull() != null) {
                                                                elem.changeProp(it.key, JSONNumber(field.text.toDouble()))
                                                            } else if (field.text.toBooleanStrictOrNull() != null) {
                                                                elem.changeProp(
                                                                    it.key,
                                                                    JSONBoolean(field.text.toBooleanStrict())
                                                                )
                                                            } else if (field.text.length == 1) {
                                                                elem.changeProp(it.key, JSONChar(field.text.toCharArray().first()))
                                                            } else {
                                                                elem.changeProp(it.key, JSONString(field.text))
                                                            }
                                                        }

                                                        override fun undo() {
                                                            if (text.toIntOrNull() != null) {
                                                                elem.changeProp(it.key, JSONNumber(text.toInt()))
                                                            } else if (text.toDoubleOrNull() != null) {
                                                                elem.changeProp(it.key, JSONNumber(text.toDouble()))
                                                            } else if (text.toBooleanStrictOrNull() != null) {
                                                                elem.changeProp(it.key, JSONBoolean(text.toBooleanStrict()))
                                                            } else if (text.length == 1) {
                                                                elem.changeProp(it.key, JSONChar(text.toCharArray().first()))
                                                            } else {
                                                                elem.changeProp(it.key, JSONString(text))
                                                            }
                                                        }
                                                    })
                                                    cmds.last().run()
                                                }
                                            }
                                        })
                                        add(field)
                                    }
                                }

                            add(panel)
                        }
                    }
                }
            }

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("R_Click")
                        val add = JButton("Add Property")
                        add.addActionListener {
                            val prop = addPropPrompt("Property", "Name", "Type")
                            if(prop != null){
                                cmds.add(object : Command {
                                    override fun run() {
                                        new = true
                                        elem.addProp(prop.first, prop.second)
                                        new = false
                                    }

                                    override fun undo() {
                                        elem.removeProp(prop.first)
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        val addList = JButton("Add List")
                        addList.addActionListener {
                            val list = addListPrompt("List", "Name", "Type")
                            if(list != null) {
                                cmds.add(object : Command {
                                    override fun run() {
                                        new = true
                                        elem.addProp(list.first, list.second)
                                        new = false
                                    }

                                    override fun undo() {
                                        elem.removeProp(list.first)
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        val addArray = JButton("Add Array")
                        addArray.addActionListener {
                            val arr = addArrayPrompt("Array", "Name", "Type")
                            if(arr != null) {
                                cmds.add(object : Command {
                                    override fun run() {
                                        new = true
                                        elem.addProp(arr.first, arr.second)
                                        new = false
                                    }

                                    override fun undo() {
                                        elem.removeProp(arr.first)
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        val addMap = JButton("Add Map")
                        addMap.addActionListener {
                            val map = addMapPrompt("Map", "Name", "Type")
                            if(map != null) {
                                cmds.add(object : Command {
                                    override fun run() {
                                        new = true
                                        elem.addProp(map.first, map.second)
                                        new = false
                                    }

                                    override fun undo() {
                                        elem.removeProp(map.first)
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        val del = JButton("Remove")
                        del.addActionListener {
                            val prop = delPrompt("Remove", "Property", elem)
                            val oldValue = elem.value[prop]
                            if(prop != null) {
                                cmds.add(object : Command {
                                    override fun run() {
                                        elem.removeProp(prop)
                                    }

                                    override fun undo() {
                                        new = true
                                        elem.addProp(prop, oldValue!!)
                                        new = false
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        menu.add(add)
                        menu.add(addList)
                        menu.add(addArray)
                        menu.add(addMap)
                        menu.add(del)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }

    fun listPanel(elem: JSONElement): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            val line = BorderFactory.createMatteBorder(5,5,5,5, Color.RED)
            border = line

            when(elem){
                is JSONCollection -> {
                    if(new) {
                        elem.addObserver(object : JSONObserver {
                            override fun update() {
                                left.removeAll()
                                left.add(JSONEditor())
                                val undo = JButton("Undo")
                                undo.alignmentX = Component.CENTER_ALIGNMENT
                                undo.addActionListener {
                                    if(cmds.isNotEmpty()){
                                        cmds.last().undo()
                                        cmds.removeLast()
                                    }
                                }
                                left.add(undo)
                                left.revalidate()
                                right.removeAll()
                                right.add(JSONText())
                                right.revalidate()
                            }
                        })
                    }

                    if(elem.value.first() is JSONObj){
                        elem.value.forEach {
                            add(objPanel(it as JSONObj))
                        }
                    }else{
                        val j = elem.value.first()
                        elem.value.forEach {
                            val field = JTextField(it.toString().replace("\"", ""))
                            val text = field.text
                            field.addFocusListener(object : FocusAdapter() {
                                override fun focusLost(e: FocusEvent) {
                                    if(field.text != text){
                                        cmds.add(object : Command {
                                            override fun run() {
                                                when(j){
                                                    is JSONString -> elem.changeValue(elem.value.indexOf(it), JSONString(field.text))
                                                    is JSONNumber -> {
                                                        if(j.value is Int && field.text.toIntOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(it), JSONNumber(field.text.toInt()))
                                                        }else if(field.text.toDoubleOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(it), JSONNumber(field.text.toDouble()))
                                                        }
                                                    }
                                                    is JSONChar -> {
                                                        if(field.text.length == 1){
                                                            elem.changeValue(elem.value.indexOf(it), JSONChar(field.text.toCharArray().first()))
                                                        }
                                                    }
                                                    is JSONBoolean -> {
                                                        if(field.text.toBooleanStrictOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(it), JSONBoolean(field.text.toBooleanStrict()))
                                                        }
                                                    }
                                                }
                                            }

                                            override fun undo() {
                                                when(j){
                                                    is JSONString -> {
                                                        elem.changeValue(elem.value.indexOf(JSONString(field.text)), JSONString(text))
                                                    }
                                                    is JSONNumber -> {
                                                        if(j.value is Int && text.toIntOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(JSONNumber(field.text.toInt())), JSONNumber(text.toInt()))
                                                        }else if(text.toDoubleOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(JSONNumber(field.text.toDouble())), JSONNumber(text.toDouble()))
                                                        }
                                                    }
                                                    is JSONChar -> {
                                                        if(text.length == 1){
                                                            elem.changeValue(elem.value.indexOf(JSONChar(field.text.toCharArray().first())), JSONChar(text.toCharArray().first()))
                                                        }
                                                    }
                                                    is JSONBoolean -> {
                                                        if(text.toBooleanStrictOrNull() != null){
                                                            elem.changeValue(elem.value.indexOf(JSONBoolean(field.text.toBooleanStrict())), JSONBoolean(text.toBooleanStrict()))
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        cmds.last().run()
                                    }
                                }
                            })
                            add(field)
                        }
                    }
                }
                is JSONArray -> {
                    if(new) {
                        elem.addObserver(object : JSONObserver {
                            override fun update() {
                                left.removeAll()
                                left.add(JSONEditor())
                                val undo = JButton("Undo")
                                undo.alignmentX = Component.CENTER_ALIGNMENT
                                undo.addActionListener {
                                    if(cmds.isNotEmpty()){
                                        cmds.last().undo()
                                        cmds.removeLast()
                                    }
                                }
                                left.add(undo)
                                left.revalidate()
                                right.removeAll()
                                right.add(JSONText())
                                right.revalidate()
                            }
                        })
                    }

                    val j = elem.value.first()
                    elem.value.forEach {
                        val field = JTextField(it.toString().replace("\"", ""))
                        val text = field.text
                        field.addFocusListener(object : FocusAdapter() {
                            override fun focusLost(e: FocusEvent) {
                                if(field.text != text){
                                    cmds.add(object : Command {
                                        override fun run() {
                                            when(j){
                                                is JSONString -> elem.changeValue(elem.value.indexOf(it), JSONString(field.text))
                                                is JSONNumber -> {
                                                    if(j.value is Int && field.text.toIntOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(it), JSONNumber(field.text.toInt()))
                                                    }else if(field.text.toDoubleOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(it), JSONNumber(field.text.toDouble()))
                                                    }
                                                }
                                                is JSONChar -> {
                                                    if(field.text.length == 1){
                                                        elem.changeValue(elem.value.indexOf(it), JSONChar(field.text.toCharArray().first()))
                                                    }
                                                }
                                                is JSONBoolean -> {
                                                    if(field.text.toBooleanStrictOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(it), JSONBoolean(field.text.toBooleanStrict()))
                                                    }
                                                }
                                            }
                                        }

                                        override fun undo() {
                                            when(j){
                                                is JSONString -> {
                                                    elem.changeValue(elem.value.indexOf(JSONString(field.text)), JSONString(text))
                                                }
                                                is JSONNumber -> {
                                                    if(j.value is Int && text.toIntOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(JSONNumber(field.text.toInt())), JSONNumber(text.toInt()))
                                                    }else if(text.toDoubleOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(JSONNumber(field.text.toDouble())), JSONNumber(text.toDouble()))
                                                    }
                                                }
                                                is JSONChar -> {
                                                    if(text.length == 1){
                                                        elem.changeValue(elem.value.indexOf(JSONChar(field.text.toCharArray().first())), JSONChar(text.toCharArray().first()))
                                                    }
                                                }
                                                is JSONBoolean -> {
                                                    if(text.toBooleanStrictOrNull() != null){
                                                        elem.changeValue(elem.value.indexOf(JSONBoolean(field.text.toBooleanStrict())), JSONBoolean(text.toBooleanStrict()))
                                                    }
                                                }
                                            }
                                        }
                                    })
                                    cmds.last().run()
                                }
                            }
                        })
                        add(field)
                    }
                }
            }

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("R_Click")
                        val add = JButton("Add Value")
                        add.addActionListener {
                            if(elem is JSONCollection){
                                if(elem.value.first() is JSONObj){
                                    val empty = JSONObj(EmptyObj("NEW OBJECT"))
                                    cmds.add(object : Command {
                                        override fun run() {
                                            new = true
                                            elem.addValue(empty)
                                            new = false
                                        }

                                        override fun undo() {
                                            elem.removeValue(empty)
                                        }
                                    })
                                    cmds.last().run()
                                }else{
                                    val value = addToColPrompt("Value", elem)
                                    if(value != null) {
                                        cmds.add(object : Command {
                                            override fun run() {
                                                elem.addValue(value)
                                            }

                                            override fun undo() {
                                                elem.removeValue(value)
                                            }
                                        })
                                        cmds.last().run()
                                    }
                                }
                            }else{
                                val value = addToColPrompt("Value", elem)
                                if(value != null) {
                                    cmds.add(object : Command {
                                        override fun run() {
                                            (elem as JSONArray).addValue(value)
                                        }

                                        override fun undo() {
                                            (elem as JSONArray).removeValue(value)
                                        }
                                    })
                                    cmds.last().run()
                                }
                            }
                            menu.isVisible = false
                        }
                        val del = JButton("Remove Value")
                        del.addActionListener {
                            val value = delFromColPrompt("Remove", elem)
                            if(value != null) {
                                if (elem is JSONCollection) {
                                    cmds.add(object : Command {
                                        override fun run() {
                                            elem.removeValue(value)
                                        }

                                        override fun undo() {
                                            elem.addValue(value)
                                        }
                                    })
                                    cmds.last().run()
                                } else {
                                    cmds.add(object : Command {
                                        override fun run() {
                                            (elem as JSONArray).removeValue(value)
                                        }

                                        override fun undo() {
                                            (elem as JSONArray).addValue(value)
                                        }
                                    })
                                    cmds.last().run()
                                }
                                menu.isVisible = false
                            }
                        }
                        menu.add(add)
                        menu.add(del)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }

    fun mapPanel(elem: JSONMap): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            val line = BorderFactory.createMatteBorder(5,5,5,5, Color.BLUE)
            border = line

            if(new) {
                elem.addObserver(object : JSONObserver {
                    override fun update() {
                        left.removeAll()
                        left.add(JSONEditor())
                        val undo = JButton("Undo")
                        undo.alignmentX = Component.CENTER_ALIGNMENT
                        undo.addActionListener {
                            if(cmds.isNotEmpty()){
                                cmds.last().undo()
                                cmds.removeLast()
                            }
                        }
                        left.add(undo)
                        left.revalidate()
                        right.removeAll()
                        right.add(JSONText())
                        right.revalidate()
                    }
                })
            }

            val j = elem.value.values.first()
            elem.value.forEach{
                add(JLabel(it.key))
                if(it.value is JSONObj){
                    add(objPanel(it.value as JSONObj))
                }else{
                    val field = JTextField(it.value.toString().replace("\"", ""))
                    val text = field.text
                    field.addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {
                            if(field.text != text){
                                cmds.add(object : Command {
                                    override fun run() {
                                        when(j){
                                            is JSONString -> elem.changeEntry(it.key, JSONString(field.text))
                                            is JSONNumber -> {
                                                if(j.value is Int && field.text.toIntOrNull() != null){
                                                    elem.changeEntry(it.key, JSONNumber(field.text.toInt()))
                                                }else if(field.text.toDoubleOrNull() != null){
                                                    elem.changeEntry(it.key, JSONNumber(field.text.toDouble()))
                                                }
                                            }
                                            is JSONChar -> {
                                                if(field.text.length == 1){
                                                    elem.changeEntry(it.key, JSONChar(field.text.toCharArray().first()))
                                                }
                                            }
                                            is JSONBoolean -> {
                                                if(field.text.toBooleanStrictOrNull() != null){
                                                    elem.changeEntry(it.key, JSONBoolean(field.text.toBooleanStrict()))
                                                }
                                            }
                                        }
                                    }

                                    override fun undo() {
                                        when(j){
                                            is JSONString -> {
                                                elem.changeEntry(it.key, JSONString(text))
                                            }
                                            is JSONNumber -> {
                                                if(j.value is Int && text.toIntOrNull() != null){
                                                    elem.changeEntry(it.key, JSONNumber(text.toInt()))
                                                }else if(text.toDoubleOrNull() != null){
                                                    elem.changeEntry(it.key, JSONNumber(text.toDouble()))
                                                }
                                            }
                                            is JSONChar -> {
                                                if(text.length == 1){
                                                    elem.changeEntry(it.key, JSONChar(text.toCharArray().first()))
                                                }
                                            }
                                            is JSONBoolean -> {
                                                if(text.toBooleanStrictOrNull() != null){
                                                    elem.changeEntry(it.key, JSONBoolean(text.toBooleanStrict()))
                                                }
                                            }
                                        }
                                    }
                                })
                                cmds.last().run()
                            }
                        }
                    })
                    add(field)
                }
            }

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("R_Click")
                        val add = JButton("Add Entry")
                        add.addActionListener {
                            if(elem.value.values.first() is JSONObj){
                                val key = JOptionPane.showInputDialog("Key")
                                val empty = JSONObj(EmptyObj("NEW OBJECT"))
                                cmds.add(object : Command {
                                    override fun run() {
                                        new = true
                                        elem.addEntry(key, empty)
                                        new = false
                                    }

                                    override fun undo() {
                                        elem.removeEntry(key)
                                    }
                                })
                                cmds.last().run()
                            }else{
                                val entry = addToMapPrompt("Entry", "Key", "Value", elem)
                                if(entry != null) {
                                    cmds.add(object : Command {
                                        override fun run() {
                                            elem.addEntry(entry.first, entry.second)
                                        }

                                        override fun undo() {
                                            elem.removeEntry(entry.first)
                                        }
                                    })
                                    cmds.last().run()
                                }
                            }
                            menu.isVisible = false
                        }
                        val del = JButton("Remove Entry")
                        del.addActionListener {
                            val entry = delFromMapPrompt("Remove", elem)
                            if(entry != null) {
                                cmds.add(object : Command {
                                    override fun run() {
                                        elem.removeEntry(entry)
                                    }

                                    override fun undo() {
                                        val oldValue = elem.value[entry]
                                        elem.addEntry(entry, oldValue!!)
                                    }
                                })
                                cmds.last().run()
                                menu.isVisible = false
                            }
                        }
                        menu.add(add)
                        menu.add(del)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }

    fun addPropPrompt(msg: String, first: String, second: String): Pair<String, JSONElement>? {
        val firstField = JTextField(5)

        val options = arrayOf("String", "Char", "Number", "Boolean", "Object")
        val secondField = JComboBox(options)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        myPanel.add(JLabel("$second:"))
        myPanel.add(secondField)

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            val selected = secondField.selectedItem as String
            var elem =
                if(selected == "String"){
                    JSONString("NEW PROPERTY")
                }else if(selected == "Char"){
                    JSONChar('!')
                }else if(selected == "Number"){
                    JSONNumber(0)
                }else if(selected == "Boolean") {
                    JSONBoolean(false)
                }else if(selected == "Object"){
                    JSONObj(EmptyObj("NEW OBJECT"))
                }else{
                    null
                }

            return Pair(firstField.text, elem!!)
        }else{
            return null
        }
    }

    fun addListPrompt(msg: String, first: String, second: String): Pair<String, JSONCollection>? {
        val firstField = JTextField(5)

        val options = arrayOf("String", "Char", "Number", "Boolean", "Object")
        val secondField = JComboBox(options)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        myPanel.add(JLabel("$second:"))
        myPanel.add(secondField)

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            val selected = secondField.selectedItem as String
            var elem =
                if(selected == "String"){
                    JSONCollection(listOf("NEW LIST"))
                }else if(selected == "Char"){
                    JSONCollection(listOf('!'))
                }else if(selected == "Number"){
                    JSONCollection(listOf(0))
                }else if(selected == "Boolean"){
                    JSONCollection(listOf(false))
                }else if(selected == "Object"){
                    JSONCollection(listOf(EmptyObj("NEW OBJECT")))
                }else{
                    null
                }

            return Pair(firstField.text, elem!!)
        }else{
            return null
        }
    }

    fun addArrayPrompt(msg: String, first: String, second: String): Pair<String, JSONArray>? {
        val firstField = JTextField(5)

        val options = arrayOf("String", "Char", "Number", "Boolean")
        val secondField = JComboBox(options)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        myPanel.add(JLabel("$second:"))
        myPanel.add(secondField)

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            val selected = secondField.selectedItem as String
            var elem =
                if(selected == "String"){
                    JSONArray(arrayOf("NEW ARRAY"))
                }else if(selected == "Char"){
                    JSONArray(arrayOf('!'))
                }else if(selected == "Number"){
                    JSONArray(arrayOf(0))
                }else if(selected == "Boolean"){
                    JSONArray(arrayOf(false))
                }else{
                    null
                }

            return Pair(firstField.text, elem!!)
        }else{
            return null
        }
    }

    fun addMapPrompt(msg: String, first: String, second: String): Pair<String, JSONMap>? {
        val firstField = JTextField(5)

        val options = arrayOf("String", "Char", "Number", "Boolean", "Object")
        val secondField = JComboBox(options)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        myPanel.add(JLabel("$second:"))
        myPanel.add(secondField)

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION) {
            val selected = secondField.selectedItem as String
            var elem =
                if (selected == "String") {
                    JSONMap(mapOf(Pair("entry", "NEW MAP")))
                } else if (selected == "Char") {
                    JSONMap(mapOf(Pair("entry", '!')))
                } else if (selected == "Number") {
                    JSONMap(mapOf(Pair("entry", 0)))
                } else if (selected == "Boolean") {
                    JSONMap(mapOf(Pair("entry", false)))
                }else if (selected == "Object"){
                    JSONMap(mapOf(Pair("entry", EmptyObj("NEW OBJECT"))))
                }else{
                    null
                }

            return Pair(firstField.text, elem!!)
        }else{
            return null
        }
    }

    fun addToColPrompt(msg: String, col: JSONElement): JSONElement? {
        val firstField = JTextField(5)

        val myPanel = JPanel()
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            val first =
                if(col is JSONCollection){
                    col.value.first()
                }else{
                    (col as JSONArray).value.first()
                }

            var elem =
                when(first){
                    is JSONString -> JSONString(firstField.text)
                    is JSONNumber -> {
                        if(first.value is Int && firstField.text.toIntOrNull() != null){
                            JSONNumber(firstField.text.toInt())
                        }else if(firstField.text.toDoubleOrNull() != null){
                            JSONNumber(firstField.text.toDouble())
                        }else{
                            null
                        }
                    }
                    is JSONChar -> {
                        if(firstField.text.length == 1){
                            JSONChar(firstField.text.toCharArray().first())
                        }else{
                            null
                        }
                    }
                    is JSONBoolean -> {
                        if(firstField.text.toBooleanStrictOrNull() != null){
                            JSONBoolean(firstField.text.toBooleanStrict())
                        }else{
                            null
                        }
                    }
                    else -> null
                }

            return elem!!
        }else{
            return null
        }
    }

    fun delFromColPrompt(msg: String, col: JSONElement): JSONElement? {
        val values =
            if(col is JSONCollection){
                col.value.toTypedArray()
            }else{
                (col as JSONArray).value.toArray()
            }
        val firstField = JComboBox(values)

        val myPanel = JPanel()
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15)) // a spacer

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            return firstField.selectedItem as JSONElement
        }else{
            return null
        }
    }

    fun addToMapPrompt(msg: String, first: String, second: String, map: JSONMap): Pair<String, JSONElement>? {
        val firstField = JTextField(5)
        val secondField = JTextField(5)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15))

        myPanel.add(JLabel("$second:"))
        myPanel.add(secondField)

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            val first = map.value.values.first()

            var elem =
                when(first){
                    is JSONString -> JSONString(secondField.text)
                    is JSONNumber -> {
                        if(first.value is Int && secondField.text.toIntOrNull() != null){
                            JSONNumber(secondField.text.toInt())
                        }else if(secondField.text.toDoubleOrNull() != null){
                            JSONNumber(secondField.text.toDouble())
                        }else{
                            null
                        }
                    }
                    is JSONChar -> {
                        if(secondField.text.length == 1){
                            JSONChar(secondField.text.toCharArray().first())
                        }else{
                            null
                        }
                    }
                    is JSONBoolean -> {
                        if(secondField.text.toBooleanStrictOrNull() != null){
                            JSONBoolean(secondField.text.toBooleanStrict())
                        }else{
                            null
                        }
                    }
                    else -> null
                }

            return Pair(firstField.text, elem!!)
        }else{
            return null
        }
    }

    fun delFromMapPrompt(msg: String, map: JSONMap): String? {
        val values = map.value.keys.toTypedArray()
        val firstField = JComboBox(values)

        val myPanel = JPanel()
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15)) // a spacer

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            return firstField.selectedItem as String
        }else{
            return null
        }
    }

    fun delPrompt(msg: String, first: String, obj: JSONObj): String? {
        val props = obj.value.keys.toTypedArray()
        val firstField = JComboBox(props)

        val myPanel = JPanel()
        myPanel.add(JLabel("$first:"))
        myPanel.add(firstField)
        myPanel.add(Box.createHorizontalStrut(15)) // a spacer

        val result = JOptionPane.showConfirmDialog(
            null, myPanel,
            msg, JOptionPane.OK_CANCEL_OPTION
        )

        if(result == JOptionPane.OK_OPTION){
            return firstField.selectedItem as String
        }else{
            return null
        }
    }
}