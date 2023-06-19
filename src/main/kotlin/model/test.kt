package model

import java.text.SimpleDateFormat
import java.util.*

fun firstTest() {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = Date()
    val current = formatter.format(date)


    val numero1= 101011
    val nome1 = "John Smith"
    val inter1 = true

    val numero2= 101013
    val nome2 = "Julia Paiva"
    val inter2 = false

    val inscrito1 = Aluno(numero1,nome1,inter1)
    val inscrito2 = Aluno(numero2,nome2,inter2)
    val listaInscritos = listOf(inscrito1,inscrito2)
    val listaCursos = listOf("MEI", "MIG")


    val obj = UC("PA",6.0, current, listaInscritos, listaCursos)
    val json = JSONObj(obj)

    println(json.toString())



}