package model

data class UC(val uc: String, val ects: Double, val dataExame: String, val inscritos: List<Aluno>, val cursos: List<String>){}