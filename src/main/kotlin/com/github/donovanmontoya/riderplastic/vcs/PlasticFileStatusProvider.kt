package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.FileStatusProvider
import com.intellij.openapi.vfs.VirtualFile

class PlasticFileStatusProvider(private val project: Project) : FileStatusProvider() {

    override fun getFileStatus(virtualFile: VirtualFile): FileStatus {
        return FileStatus.NOT_CHANGED
    }
}
