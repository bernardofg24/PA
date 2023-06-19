package gui

interface FieldObserver {
    fun changeValue(prop: String, newValue: Any){}
}