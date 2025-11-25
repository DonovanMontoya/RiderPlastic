package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.checkin.CheckinEnvironment
import com.intellij.openapi.vcs.checkin.CommitContext
import com.intellij.openapi.vcs.checkin.VcsCallable
import com.intellij.openapi.vcs.ui.RefreshableOnComponent

class PlasticCheckinEnvironment(private val project: Project) : CheckinEnvironment {

    private val runner = project.getService(PlasticCommandRunner::class.java)

    override fun commit(changes: List<com.intellij.openapi.vcs.changes.Change>, commitMessage: String, parameters: CommitContext?, feedback: Runnable?): List<VcsException> {
        runner?.run(PlasticCommand.COMMIT, "-c=$commitMessage")
        feedback?.run()
        return emptyList()
    }

    override fun scheduleMissingFileForDeletion(files: MutableList<com.intellij.openapi.vfs.VirtualFile>): MutableList<VcsException> = mutableListOf()

    override fun scheduleUnversionedFilesForAddition(files: MutableList<com.intellij.openapi.vfs.VirtualFile>): MutableList<VcsException> = mutableListOf()

    override fun getDefaultMessageFor(filesToCheckin: MutableList<com.intellij.openapi.vcs.FilePath>): String? = null

    override fun getHelpId(): String? = null

    override fun getAdditionalOperationUI(): RefreshableOnComponent? = null

    override fun isRefreshAfterCommitNeeded(): Boolean = true

    override fun isDeleteLocallyAddedFilesOnRevert(): Boolean = false

    override fun createCommitOptions(changes: MutableList<com.intellij.openapi.vcs.changes.Change>, commitMessage: String?): Array<out VcsCallable> = emptyArray()
}
