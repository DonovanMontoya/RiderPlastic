package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ContentRevision
import com.intellij.openapi.vcs.DiffProvider
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.VcsUtil
import com.intellij.openapi.vcs.changes.CurrentContentRevision
import com.intellij.openapi.vcs.history.VcsRevisionNumber

class PlasticDiffProvider(private val project: Project) : DiffProvider {

    override fun getCurrentRevision(file: com.intellij.openapi.vfs.VirtualFile): VcsRevisionNumber? {
        return PlasticRevisionNumber(project, file.path)
    }

    override fun getLastRevision(file: FilePath): VcsRevisionNumber? {
        return PlasticRevisionNumber(project, file.path)
    }

    override fun createFileContent(revisionNumber: VcsRevisionNumber?, selectedFile: com.intellij.openapi.vfs.VirtualFile): ContentRevision? {
        return CurrentContentRevision(VcsUtil.getFilePath(selectedFile))
    }

    override fun getLatestCommittedRevision(file: FilePath): VcsRevisionNumber? = getLastRevision(file)

    private class PlasticRevisionNumber(private val project: Project, private val id: String) : VcsRevisionNumber {
        override fun asString(): String = id
        override fun compareTo(other: VcsRevisionNumber): Int = asString().compareTo(other.asString())
        override fun getTimestamp(): Long = System.currentTimeMillis()
    }
}
