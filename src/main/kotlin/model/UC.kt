package model

//classe criada para teste (documento test.kt), usada na classe "Aluno"

data class UC(val uc: String, val ects: Double, val dataExame: String, val inscritos: List<Aluno>){}