import model.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Tests {

    @Test
    fun stringTest(){
        val str = "\"String\""
        assertEquals(str, JSONString("String").toString())
    }

    @Test
    fun intTest(){
        val int = "24"
        assertEquals(int, JSONNumber(24).toString())
    }

    @Test
    fun doubleTest(){
        val double = "8.1"
        assertEquals(double, JSONNumber(8.1).toString())
    }

    @Test
    fun charTest(){
        val char = "r"
        assertEquals(char, JSONChar('r').toString())
    }

    @Test
    fun boolTest(){
        val bool = "true"
        assertEquals(bool, JSONBoolean(true).toString())
    }

    @Test
    fun arrayTest(){
        val arr = "[\n" +
                "\t\"Unit\",\n" +
                "\t\"Test\"\n" +
                "]"
        assertEquals(arr, JSONArray(arrayOf("Unit", "Test")).toString())
    }

    @Test
    fun listTest(){
        val list = "[\n" +
                "\t\"Unit\",\n" +
                "\t\"Test\"\n" +
                "]"
        assertEquals(list, JSONCollection(listOf("Unit", "Test")).toString())
    }

    @Test
    fun mapTest(){
        val map = "{\n" +
                "\t\"Teste\": \"Unitário\",\n" +
                "\t\"Unit\": \"Test\"\n" +
                "}"
        assertEquals(map, JSONMap(mapOf(Pair("Teste", "Unitário"), Pair("Unit", "Test"))).toString())
    }
}