
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
        srcArea.isEditable=false
        srcArea.text="ds"

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
                    val fList=valueUC !!!!
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
            val text = JTextField(""+valueCreditos)
            text.size = Dimension(10,1)

            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueCreditos !!!!
                }
            })

            add(text)
        }

    fun exameWidget(keyExame:String, valueExame:String): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            add(JLabel(keyExame))
            val text = JTextField(""+valueExame)
            text.isEditable=false

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
            //val textNom

        }
}