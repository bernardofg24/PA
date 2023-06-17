import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*
import javax.swing.text.BoxView



fun main() {
    Editor().open()
}

class Editor {
    val frame = JFrame("Josue - JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(900, 750)



        //ESQUERDO
        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(rightPanel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)



        //DIREITO
        val right = JPanel()
        right.layout = GridLayout()
        val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.text = "{"

        right.add(srcArea)
        add(right)
    }

    fun open() {
        frame.isVisible = true
    }

    fun rightPanel(): JPanel =
        JPanel().apply {

            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT



            var UnidCurricular = UCWidget("uc","PA")
            var creditos = creditosWidget("ects", 6.0)
            var exame = ExameWidget("exame", false)
            var inscritos = inscritosWidget("inscritos","nome", "John Smith", "numero",
                83993,"internacional",false)

            add(UnidCurricular)
            add(creditos)
            add(exame)
            add(inscritos)




            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")
                        add.addActionListener {
                            val text = JOptionPane.showInputDialog("text")
                            add(testWidget(text, "?"))
                            menu.isVisible = true
                            revalidate()
                            frame.repaint()
                        }
                        val del = JButton("delete all")
                        del.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = true
                            revalidate()
                            frame.repaint()
                        }
                        menu.add(add);
                        menu.add(del)
                        menu.show(this@apply, 100, 100);
                    }
                }
            })
        }


    fun testWidget(key: String, value: String): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            add(JLabel(key))
            val text = JTextField(value)
            text.setBounds(10,10,1,2000000)
            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${text.text}")
                }
            })
            add(text)
        }

    fun UCWidget(keyUC:String, valueUC:String): JPanel=
        JPanel().apply {
            //layout = BoxLayout(this, BoxLayout.X_AXIS)

            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            size = Dimension(20,5)


            add(JLabel(keyUC))
            val text = JTextField(valueUC)

            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueUC !!!!
                }
            })

            add(text)
        }

    fun creditosWidget(keyCreditos:String, valueCreditos:Number): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(keyCreditos))
            val text = JTextField(""+valueCreditos)
            text.size = Dimension(10,1)

            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueCreditos !!!!
                }
            })

            add(text)
        }

    fun ExameWidget(keyExame:String, valueExame:Boolean): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(keyExame))
            val text = JTextField(""+valueExame)

            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueExame !!!!
                }
            })

            add(text)
        }

    fun inscritosWidget(nomeinscritos: String, nomeKey: String, nomeValue:String,
                        numeroKey: String,numeroValue: Number, internacionalKey: String, internacionalValue: Boolean): JPanel=
        JPanel().apply{
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            add(JLabel(nomeinscritos))
            JPanel().apply{
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                alignmentX = Component.RIGHT_ALIGNMENT
                alignmentY = Component.TOP_ALIGNMENT
                add(JLabel(nomeKey))
                add(JLabel(numeroKey))

                val text = JTextField(""+nomeValue)

                text.addFocusListener(object : FocusAdapter() {
                    override fun focusLost(e: FocusEvent) {
                        val fList=nomeValue !!!!
                    }
                })

                add(text)






            }

        }
}
