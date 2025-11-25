package com.github.donovanmontoya.riderplastic.actions

import com.github.donovanmontoya.riderplastic.MyBundle
import com.github.donovanmontoya.riderplastic.vcs.PlasticCommand
import com.github.donovanmontoya.riderplastic.vcs.PlasticCommandRunner
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile

abstract class PlasticActionBase(private val command: PlasticCommand) : AnAction(), DumbAware {

    override fun update(e: AnActionEvent) {
        val project = e.project
        val presentation = e.presentation
        presentation.isEnabledAndVisible = project != null
        presentation.text = MyBundle.message("plastic.action.text", command.description)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val runner = project.getService(PlasticCommandRunner::class.java)
        val targetPath = resolveTargetPath(e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
        if (targetPath != null) {
            runner?.run(command, targetPath)
        } else {
            runner?.run(command)
        }
    }

    private fun resolveTargetPath(files: Array<VirtualFile>?): String? {
        return files?.firstOrNull()?.path
    }
}
