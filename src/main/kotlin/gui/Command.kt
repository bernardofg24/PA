package gui

interface Command {
    fun run(){}

    fun undo(){}
}