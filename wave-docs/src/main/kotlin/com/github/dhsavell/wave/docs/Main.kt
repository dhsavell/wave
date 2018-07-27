package com.github.dhsavell.wave.docs

import com.github.dhsavell.wave.app.provider.ManagerModule
import com.github.dhsavell.wave.core.command.ArgParserCommand
import com.github.dhsavell.wave.core.command.Command
import picocli.CommandLine
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.Callable

fun getExpectedDocLocation(root: Path, command: Command): Path {
    return root.resolve(Paths.get(command.category.name, command.name + ".md"))
}

fun createDocStub(command: Command): String {
    val documentation = StringBuilder("# ${command.name}\n")

    if (command.aliases.isNotEmpty()) {
        documentation.appendln("**Aliases**: ${command.aliases.joinToString(", ")}")
        documentation.appendln()
    }

    documentation.appendln("This is an automatically generated documentation file. " +
            "It probably needs to be updated manually.")
    documentation.appendln()

    if (command is ArgParserCommand<*>) {
        val commandLine = CommandLine(command.get())
        val parameters = commandLine.commandSpec.positionalParameters()
        val options = commandLine.commandSpec.options()

        documentation.appendln("## Usage")
        documentation.appendln()

        if (parameters.isNotEmpty()) {
            documentation.appendln("### Parameters")
            documentation.appendln("| Name | Description |")
            documentation.appendln("|------|-------------|")
            documentation.appendln(parameters.joinToString("\n") { paramSpec ->
                "| ${paramSpec.paramLabel().removeSurrounding("<", ">")} | ${paramSpec.description().joinToString(" ")} |"
            })
            documentation.appendln()
        }

        if (options.isNotEmpty()) {
            documentation.appendln("### Options")
            documentation.appendln("| Name | Required? | Description |")
            documentation.appendln("|------|-----------|-------------|")
            documentation.appendln(options.joinToString("\n") { optionSpec ->
                "| ${optionSpec.names().joinToString(", ") { "`$it`" }} | " +
                        "${if (optionSpec.required()) "Yes" else "No"} | ${optionSpec.description().joinToString(" ")}"
            })
            documentation.appendln()
        }
    }

    return documentation.toString()
}

@CommandLine.Command
class MainCli : Callable<Unit> {
    @CommandLine.Parameters(paramLabel = "output_folder", description = ["markdown documentation output directory"])
    lateinit var outputFolder: Path

    override fun call() {
        if (!Files.exists(outputFolder) or !Files.isDirectory(outputFolder)) {
            Files.createDirectories(outputFolder)
        }

        val commands = ManagerModule().provideCommandManager().commands
        commands.forEach { command ->
            val docPath = getExpectedDocLocation(outputFolder, command)
            if (Files.exists(docPath)) {
                println("File $docPath already exists, skipping...")
            } else {
                Files.createDirectories(docPath.parent)
                Files.write(docPath,
                        createDocStub(command).toByteArray(Charset.forName("utf-8")),
                        StandardOpenOption.CREATE)
                println("Created stub documentation for ${command.name} at $docPath")
            }
        }
    }
}

fun main(args: Array<String>) {
    CommandLine.call(MainCli(), *args)
}