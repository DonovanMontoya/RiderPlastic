package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vfs.VirtualFile

class PlasticFileStatusProvider(private val project: Project) {

    fun getFileStatus(virtualFile: VirtualFile): FileStatus {
        val runner = project.getService(PlasticCommandRunner::class.java) ?: return FileStatus.UNKNOWN
        val output = runner.run(PlasticCommand.STATUS, "--short", virtualFile.path) ?: return FileStatus.UNKNOWN

        if (output.exitCode != 0) {
            return FileStatus.UNKNOWN
        }

        val statusLine = output.stdout.lineSequence().firstOrNull { it.isNotBlank() }?.trim() ?: return FileStatus.NOT_CHANGED
        val statusCode = statusLine.split(Regex("\\s+"), limit = 2).firstOrNull()?.uppercase() ?: return FileStatus.NOT_CHANGED

        return when (statusCode) {
            "CH", "CO", "LD", "LM" -> FileStatus.MODIFIED
            "AD" -> FileStatus.ADDED
            "DE" -> FileStatus.DELETED
            "IG" -> FileStatus.IGNORED
            "PR", "??" -> FileStatus.UNKNOWN
            else -> FileStatus.NOT_CHANGED
        }
    }
}
