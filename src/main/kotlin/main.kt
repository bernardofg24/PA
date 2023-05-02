fun main(){
    val arr = arrayOf(1,2,3)
    val teste = Teste("ola", 24, arr)
    val json = JSONObj(teste)
    println(json.toString())
}