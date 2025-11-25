package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.changes.ContentRevision
import com.intellij.openapi.vcs.diff.DiffProvider
import com.intellij.openapi.vcs.diff.ItemLatestState
import com.intellij.openapi.vcs.history.VcsRevisionNumber
import com.intellij.openapi.vfs.VirtualFile

class PlasticDiffProvider(private val project: Project) : DiffProvider {

    override fun getCurrentRevision(file: VirtualFile): VcsRevisionNumber? {
        return PlasticRevisionNumber(file.path)
    }

    override fun getLastRevision(file: FilePath): ItemLatestState? {
        return ItemLatestState(PlasticRevisionNumber(file.path), true, false)
    }

    override fun getLastRevision(file: VirtualFile): ItemLatestState? {
        return ItemLatestState(PlasticRevisionNumber(file.path), true, false)
    }

    override fun getLatestCommittedRevision(file: VirtualFile): VcsRevisionNumber? {
        return PlasticRevisionNumber(file.path)
    }

    override fun createFileContent(revisionNumber: VcsRevisionNumber?, selectedFile: VirtualFile): ContentRevision? {
        // Return null for now - can be implemented later to show file content at a specific revision
        return null
    }

    private class PlasticRevisionNumber(private val id: String) : VcsRevisionNumber {
        override fun asString(): String = id
        override fun compareTo(other: VcsRevisionNumber): Int = asString().compareTo(other.asString())
    }
}
