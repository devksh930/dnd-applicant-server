package ac.dnd.server.review.domain.repository

import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.domain.model.Project
import ac.dnd.server.review.domain.model.ProjectUrl

interface ProjectRepository {
    fun save(project: Project): Project
    fun saveAllUrls(projectId: Long, projectUrls: List<ProjectUrl>)
    fun deleteUrlsByProjectId(projectId: Long)
    fun findProjectByLinkKey(linkKey: String): Project?
    fun saveFormLink(formLink: FormLink): FormLink
    fun findLinkByLinkKey(linkKey: String): FormLink?
}
