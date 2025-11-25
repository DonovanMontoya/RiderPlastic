package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.AbstractVcs
import com.intellij.openapi.vcs.CheckinEnvironment
import com.intellij.openapi.vcs.DiffProvider
import com.intellij.openapi.vcs.FileStatusProvider
import com.intellij.openapi.vcs.UpdateEnvironment
import com.intellij.openapi.vcs.VcsKey
import com.intellij.openapi.vfs.VirtualFile

class PlasticVcs(project: Project) : AbstractVcs(project, NAME) {

    private val checkinEnvironment = PlasticCheckinEnvironment(project)
    private val updateEnvironment = PlasticUpdateEnvironment(project)
    private val diffProvider = PlasticDiffProvider(project)
    private val fileStatusProvider = PlasticFileStatusProvider(project)

    override fun getCheckinEnvironment(): CheckinEnvironment = checkinEnvironment

    override fun getUpdateEnvironment(): UpdateEnvironment = updateEnvironment

    override fun createDiffProvider(): DiffProvider = diffProvider

    override fun getFileStatusProvider(): FileStatusProvider = fileStatusProvider

    override fun getVcsKey(): VcsKey = VcsKey.create(NAME)

    override fun getDisplayName(): String = "Plastic SCM"

    override fun isVersionedDirectory(dir: VirtualFile): Boolean = true

    companion object {
        const val NAME = "PlasticSCM"
    }
}
