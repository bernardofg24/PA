class JSONObj(obj: Any) : JSONElement{
    private val clazz = obj::class
    val props = LinkedHashMap<String, JSONElement>()

    init{

    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}