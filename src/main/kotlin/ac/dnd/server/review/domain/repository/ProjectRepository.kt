package ac.dnd.server.review.domain.repository

import ac.dnd.server.review.infrastructure.persistence.entity.Project
import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrl

interface ProjectRepository {
    fun save(project: Project): Project
    fun saveAllUrls(projectUrls: List<ProjectUrl>): List<ProjectUrl>
    fun deleteUrlsByProjectId(projectId: Long)
    fun findProjectByLinkKey(linkKey: String): Project?
    fun saveFormLink(formLink: FormLink): FormLink
    fun findLinkByLinkKey(linkKey: String) :FormLink?
}
