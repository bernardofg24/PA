fun main(){
    val arr = listOf(listOf(1,2,34), listOf(6))
    val obj = Teste("ola", 24, arr)
    val teste = Teste2("ola", 24, obj)
    val json = JSONObj(obj)
    println(json.toString())
}