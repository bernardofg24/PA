fun main(){
    val map = mapOf(1 to "um", 2 to "dois", 3 to "três")
    val obj = Teste("ola", 24, Please.TESTE)
    val teste = Teste2("ola", 24, obj)
    val json = JSONObj(obj)
    println(json.toString())
}