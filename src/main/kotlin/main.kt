fun main(){
    val map = mapOf(1 to "um", 2 to "dois", 3 to "três")
    val obj = Teste("adeus", 42, listOf("asd", "fgh"))
    val teste = Teste2("ola", 24, obj)
    val json = JSONObj(teste)
    val vis = SearchVisitor(listOf("name"), true)
    json.accept(vis)
    print(vis.values)
}