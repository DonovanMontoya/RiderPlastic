package com.github.donovanmontoya.riderplastic.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.AbstractVcs
import com.intellij.openapi.vcs.checkin.CheckinEnvironment
import com.intellij.openapi.vcs.diff.DiffProvider
import com.intellij.openapi.vcs.update.UpdateEnvironment
import com.intellij.openapi.vfs.VirtualFile

class PlasticVcs(project: Project) : AbstractVcs(project, NAME) {

    private val checkinEnvironment = PlasticCheckinEnvironment(project)
    private val updateEnvironment = PlasticUpdateEnvironment(project)
    private val diffProvider = PlasticDiffProvider(project)

    override fun getCheckinEnvironment(): CheckinEnvironment = checkinEnvironment

    override fun getUpdateEnvironment(): UpdateEnvironment = updateEnvironment

    override fun getDiffProvider(): DiffProvider = diffProvider

    override fun getDisplayName(): String = "Plastic SCM"

    override fun isVersionedDirectory(dir: VirtualFile): Boolean = true

    companion object {
        const val NAME = "PlasticSCM"
    }
}
