package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.checkin.CheckinEnvironment
import com.intellij.openapi.vfs.VirtualFile

class PlasticCheckinEnvironment(private val project: Project) : CheckinEnvironment {

    private val runner = project.getService(PlasticCommandRunner::class.java)

    override fun commit(changes: MutableList<out Change>, commitMessage: String): MutableList<VcsException>? {
        val exceptions = mutableListOf<VcsException>()
        val changePaths = changes.mapNotNull { change ->
            change.afterRevision?.file?.path ?: change.beforeRevision?.file?.path
        }

        val arguments = mutableListOf("-c=$commitMessage")
        arguments.addAll(changePaths)

        val output = runner?.run(PlasticCommand.COMMIT, *arguments.toTypedArray())

        if (output == null) {
            exceptions.add(VcsException("Failed to execute Plastic SCM commit command."))
        } else if (output.exitCode != 0) {
            exceptions.add(VcsException(output.stderr.ifBlank { output.stdout }.ifBlank { "Plastic SCM commit failed with exit code ${output.exitCode}" }))
        }

        return if (exceptions.isEmpty()) null else exceptions
    }

    override fun scheduleMissingFileForDeletion(files: MutableList<out FilePath>): MutableList<VcsException>? = null

    override fun scheduleUnversionedFilesForAddition(files: MutableList<out com.intellij.openapi.vfs.VirtualFile>): MutableList<VcsException>? = null

    override fun getDefaultMessageFor(filesToCheckin: Array<out FilePath>): String? = null

    override fun getCheckinOperationName(): String = "Checkin"

    override fun getHelpId(): String? = null

    override fun isRefreshAfterCommitNeeded(): Boolean = true
}
