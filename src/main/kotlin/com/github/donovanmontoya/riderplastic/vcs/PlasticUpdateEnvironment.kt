package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.UpdateEnvironment
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.update.FileGroup
import com.intellij.openapi.vcs.update.UpdatedFiles

class PlasticUpdateEnvironment(private val project: Project) : UpdateEnvironment {

    private val runner = project.getService(PlasticCommandRunner::class.java)

    override fun updateDirectories(contentRoots: MutableCollection<FilePath>, updatedFiles: UpdatedFiles, progressIndicator: ProgressIndicator, force: MutableList<VcsException>, modifiableFiles: MutableList<FilePath>): com.intellij.openapi.vcs.update.UpdateSession {
        val output = runner?.run(PlasticCommand.CHECKOUT)
        when {
            output == null -> force.add(VcsException("Failed to execute Plastic SCM checkout command."))
            output.isCancelled -> force.add(VcsException("Plastic SCM checkout was cancelled."))
            output.isTimeout -> force.add(VcsException("Plastic SCM checkout timed out."))
            output.exitCode != 0 -> force.add(
                VcsException(
                    output.stderr.ifBlank { output.stdout }
                        .ifBlank { "Plastic SCM checkout failed with exit code ${output.exitCode}" }
                )
            )
        }
        return object : com.intellij.openapi.vcs.update.UpdateSession {
            override fun getExceptions(): MutableCollection<VcsException> = force
            override fun getStatus(): com.intellij.openapi.vcs.update.UpdateSession.SessionStatus = when {
                output?.isCancelled == true -> com.intellij.openapi.vcs.update.UpdateSession.SessionStatus.CANCEL
                force.isEmpty() -> com.intellij.openapi.vcs.update.UpdateSession.SessionStatus.SUCCESS
                else -> com.intellij.openapi.vcs.update.UpdateSession.SessionStatus.ERROR
            }
            override fun cancel(): Boolean = false
        }
    }

    override fun fillGroups(updatedFiles: UpdatedFiles) {
        updatedFiles.registerGroup(FileGroup.COMPILED)
    }

    override fun isEmptyFileSet(content: MutableCollection<FilePath>): Boolean = content.isEmpty()
}
