import java.io.File
import java.io.FileNotFoundException

private fun String.printlnIt() = println(this)
private fun String.reply() = this.printlnIt().run { readln() }
private fun String.formattedToList() = this.uppercase().split(Regex("\\s+"))

private enum class MatchingStrategy { ALL, ANY, NONE }

fun main(vararg args: String) {

    if (args.size != 2 && args[0] != "--data") throw IllegalArgumentException("Invalid arguments.")
    val file = File(args[1]).also { if (!it.exists() || it.isDirectory) throw FileNotFoundException("File <${it.name}> not found or is a directory.") }

    val fileLines = file.readLines()

    while (true) {
        when ("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit".reply()) {
            "0" -> break
            "1" -> {
                val matchingStrategy = MatchingStrategy.valueOf("Select a matching strategy: ALL, ANY, NONE".reply())

                val query = "Enter a name or email to search all matching people.".reply()

                val queryResult = when (matchingStrategy) {
                    MatchingStrategy.ALL -> fileLines.filter { line -> line.formattedToList().all { it in query.formattedToList() } }
                    MatchingStrategy.ANY -> fileLines.filter { line -> line.formattedToList().any { it in query.formattedToList() } }
                    MatchingStrategy.NONE -> fileLines.filter { line -> line.formattedToList().none { it in query.formattedToList() } }
                }

                queryResult.run {
                    joinToString("\n", if (isEmpty()) "No matching people found." else "$size person${if (size > 1) "s" else ""} found:\n")
                }.printlnIt()
            }
            "2" -> "=== List of people ===\n${File(args[1]).readText()}".printlnIt()
            else -> "Incorrect option! Try again.".printlnIt()
        }
    }

    println("Bye!")
}
