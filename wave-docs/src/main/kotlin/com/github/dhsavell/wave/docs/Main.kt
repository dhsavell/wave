package com.github.dhsavell.wave.docs

import com.github.dhsavell.wave.app.provider.ManagerModule
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