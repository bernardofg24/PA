package gui

import model.Aluno
import model.JSONObj
import model.UC

fun main() {
    val obj = UC("PA", 6.0, "21-06-2023", listOf(Aluno(93251, "Bernardo Grilo", false)), listOf("MEI", "MIG", "METI"))
    val model = JSONObj(obj)

    val frame = Editor(model)
    frame.open()
}