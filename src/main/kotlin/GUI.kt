
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.*


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

            val UnidadeCurricular = UCWidget("UC ","PA")
            val bound = UnidadeCurricular.setBounds(20,5,450,50)
            add(UnidadeCurricular)

            val creditos = creditosWidget("ECTS ", 6.0)
            add(creditos)

            val exame = exameWidget("exame ","null")
            add(exame)

            val inscritos = inscritosWidget("inscritos ", inscritosCorpo(numeroInscritos("número ","11029"),
                nomeInscritos("nome ","John Smith"),internacionalInscritos("internacional")))
            add(inscritos)


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
                    val fList=valueUC
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
                    val fList=valueCreditos
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
                    val fList=valueExame
                }
            })

            add(text)
        }

    fun inscritosWidget(keyInscritos: String, inscritosCorpo: JPanel): JPanel=
        JPanel().apply{
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            add(JLabel(keyInscritos))
            add(inscritosCorpo)

            //val textNom

        }

    fun nomeInscritos(keyNome: String, valueNome:String): JPanel=
        JPanel().apply {
            /*layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.RIGHT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT*/


            add(JLabel(keyNome))
            val text = JTextField(""+valueNome)


            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueNome
                }
            })

            add(text)
        }

    fun numeroInscritos(keyNumero: String, valueNumero:String): JPanel=
        JPanel().apply {
            /*layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.CENTER_ALIGNMENT
            alignmentY = Component.CENTER_ALIGNMENT*/


            add(JLabel(keyNumero))
            val text = JTextField(""+valueNumero)



            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    val fList=valueNumero
                }
            })

            add(text)
        }
    fun internacionalInscritos(keyInternacional:String): JPanel=
        JPanel().apply{
            add(JLabel(keyInternacional))
            val internacionalButton = JCheckBox("", true)
            add(internacionalButton)

        }
    fun inscritosCorpo(numeroInscritos:JPanel,nomeInscritos:JPanel,internacionalInscritos:JPanel): JPanel=
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.CENTER_ALIGNMENT
            alignmentY = Component.CENTER_ALIGNMENT
            add(numeroInscritos);
            add(nomeInscritos);
            add(internacionalInscritos);
        }


}