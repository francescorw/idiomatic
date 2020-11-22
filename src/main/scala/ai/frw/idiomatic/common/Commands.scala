package ai.frw.idiomatic.common

object Commands {
    val start = "start"
    val commands = "commands"
    val cancel = "cancel"
    val addGif = "addgif"

    def getAvailableCommands: String = {
        s"""
        |/${addGif} - adds a gif
        |/${cancel} - cancels the current operation
        """.stripMargin('|')
    }
}