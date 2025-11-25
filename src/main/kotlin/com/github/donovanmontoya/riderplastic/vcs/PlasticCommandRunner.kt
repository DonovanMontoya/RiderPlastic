package com.github.donovanmontoya.riderplastic.vcs

import com.github.donovanmontoya.riderplastic.MyBundle
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.nio.charset.StandardCharsets
import java.time.Duration

@Service(Service.Level.PROJECT)
class PlasticCommandRunner(private val project: Project) {

    private val log = Logger.getInstance(PlasticCommandRunner::class.java)

    fun run(command: PlasticCommand, vararg arguments: String): ProcessOutput? {
        val commandLine = GeneralCommandLine(listOf("cm", command.cliSubcommand) + arguments)
        commandLine.charset = StandardCharsets.UTF_8
        project.basePath?.let { commandLine.withWorkDirectory(it) }

        return try {
            val handler = CapturingProcessHandler(commandLine)
            val output = handler.runProcess(Duration.ofSeconds(10).toMillis().toInt())
            notify(command, output)
            output
        } catch (t: Throwable) {
            log.warn("Failed to execute Plastic SCM command ${command.cliSubcommand}", t)
            notifyFailure(command, t.message ?: t.javaClass.simpleName)
            null
        }
    }

    private fun notify(command: PlasticCommand, output: ProcessOutput) {
        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("Plastic SCM")
        val type = if (output.exitCode == 0) NotificationType.INFORMATION else NotificationType.WARNING
        val title = MyBundle.message("plastic.command.title", command.name)
        val message = buildString {
            append(MyBundle.message("plastic.command.summary", command.description, output.exitCode))
            if (output.stdout.isNotBlank()) {
                append("\n\n").append(output.stdout.trim())
            }
            if (output.stderr.isNotBlank()) {
                append("\n\n").append(output.stderr.trim())
            }
        }
        notificationGroup.createNotification(title, message, type).notify(project)
    }

    private fun notifyFailure(command: PlasticCommand, message: String) {
        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("Plastic SCM")
        val title = MyBundle.message("plastic.command.title", command.name)
        notificationGroup.createNotification(title, message, NotificationType.ERROR).notify(project)
    }
}
