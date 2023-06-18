import java.awt.CardLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*
import javax.swing.text.BoxView

fun main() {
    GUI().open()
}
class GUI {
    val guiFrame = JFrame("JSON Object Editor").apply{
        size= Dimension(900,750)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0,2)

        //Painel esquerdo
        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(leftPanel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)


        //Painel Direito
        val right=JPanel()
        right.layout = GridLayout()
        val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.isEditable=false
        srcArea.text="ds" //TODO

        right.add(srcArea)
        add(right)
    }

    fun open() {
        guiFrame.isVisible = true
    }

    fun leftPanel(): JPanel=
        JPanel().apply {

            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val UnidadeCurricular = UCWidget("UC","PA")
            val bound = UnidadeCurricular.setBounds(20,5,450,50)
            add(UnidadeCurricular)

            val creditos = creditosWidget("ECTS", 6.0)
            add(creditos)

            val exame = exameWidget("exame","null")
            add(exame)

            val inscritos = inscritosWidget("inscrito",numeroInsctrito("número",28282),
                nomeInscrito("nome", "John Smith"), interInsctrito("internacional") );
            add(inscritos)

            val curso = cursoWidget("curso","MEI")
            add(curso)



            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")
                        add.addActionListener {
                            val text = JOptionPane.showInputDialog("Component name")
                            add(UCWidget(text, "?"))
                            menu.isVisible = false
                            revalidate()
                            guiFrame.repaint()
                        }
                        val del = JButton("delete all")
                        del.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = false
                            revalidate()
                            guiFrame.repaint()
                        }
                        menu.add(add);
                        menu.add(del)
                        menu.show(this@apply, 100, 100);
                    }
                }
            })
        }



    fun UCWidget(keyUC:String, valueUC:String): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT



            add(JLabel(keyUC))
            val textUC = JTextField(valueUC)
            textUC.size=Dimension(5,200)

            textUC.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${textUC.text}")
                    //val fList=valueUC !!!!
                }
            })

            add(textUC)
        }

    fun creditosWidget(keyCreditos:String, valueCreditos:Number): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(keyCreditos))
            val textCred = JTextField(""+valueCreditos)
            textCred.size = Dimension(10,1)

            textCred.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${textCred.text}")
                    //val fList=valueCreditos !!!!
                }
            })

            add(textCred)
        }

    fun exameWidget(keyExame:String, valueExame:String): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            add(JLabel(keyExame))
            val textEx = JTextField(""+valueExame)
            textEx.isEditable=false

            textEx.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${textEx.text}")
                    //val fList=valueExame !!!!
                }
            })

            add(textEx)
        }

    fun nomeInscrito(keyNome:String, valueNome:String): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            add(JLabel(keyNome))
            val textIns = JTextField(""+valueNome)

            textIns.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    //val fList=valueNome !!!!
                    println("perdeu foco: ${textIns.text}")
                }
            })

            add(textIns)
        }

    fun numeroInsctrito(keyNum:String, valueNum:Number): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            add(JLabel(keyNum))
            val nInsText = JTextField(""+valueNum)

            nInsText.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    //val fList=valueNum !!!!
                    println("perdeu foco: ${nInsText.text}")
                }
            })

            add(nInsText)
        }

    fun interInsctrito(keyInter:String): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)



            add(JLabel(keyInter))
            val inter = JCheckBox("")

            /*text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueNum !!!!
                }
            })*/

            add(inter)
        }

    fun inscritosWidget(inscritos: String, numeroInscrito: JPanel, nomeInscrito:JPanel, interInscrito:JPanel): JPanel=
        JPanel().apply{
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            add(JLabel(inscritos))

            add(numeroInscrito)
            add(nomeInscrito)
            add(interInscrito)
            //val textNom

        }

    fun cursoWidget(keyCurso: String, valueCurso: String):JPanel=
    JPanel().apply{
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        alignmentX = Component.RIGHT_ALIGNMENT
        alignmentY = Component.TOP_ALIGNMENT


        add(JLabel(keyCurso))
        val textCurso = JTextField(""+valueCurso)

        textCurso.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                //val fList=valueCurso !!!!
                println("perdeu foco: ${textCurso.text}")
            }
        })

        add(textCurso)
    }




}