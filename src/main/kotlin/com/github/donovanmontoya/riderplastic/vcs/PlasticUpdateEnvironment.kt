package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.update.SequentialUpdatesContext
import com.intellij.openapi.vcs.update.UpdateEnvironment
import com.intellij.openapi.vcs.update.UpdateSession
import com.intellij.openapi.vcs.update.UpdatedFiles

class PlasticUpdateEnvironment(private val project: Project) : UpdateEnvironment {

    private val runner = project.getService(PlasticCommandRunner::class.java)

    override fun fillGroups(updatedFiles: UpdatedFiles) {
        // Groups will be filled as files are updated
    }

    override fun updateDirectories(
        contentRoots: Array<out FilePath>,
        updatedFiles: UpdatedFiles,
        progressIndicator: ProgressIndicator,
        context: Ref<SequentialUpdatesContext>
    ): UpdateSession {
        val exceptions = mutableListOf<VcsException>()
        val output = runner?.run(PlasticCommand.CHECKOUT)

        when {
            output == null -> exceptions.add(VcsException("Failed to execute Plastic SCM checkout command."))
            output.isCancelled -> exceptions.add(VcsException("Plastic SCM checkout was cancelled."))
            output.isTimeout -> exceptions.add(VcsException("Plastic SCM checkout timed out."))
            output.exitCode != 0 -> exceptions.add(
                VcsException(
                    output.stderr.ifBlank { output.stdout }
                        .ifBlank { "Plastic SCM checkout failed with exit code ${output.exitCode}" }
                )
            )
        }

        return object : UpdateSession {
            override fun getExceptions(): MutableList<VcsException> = exceptions

            override fun isCanceled(): Boolean = output?.isCancelled == true

            override fun onRefreshFilesCompleted() {}
        }
    }

    override fun validateOptions(contentRoots: MutableCollection<FilePath>): Boolean = true

    override fun createConfigurable(contentRoots: MutableCollection<FilePath>): Configurable? = null
}
