package ac.dnd.server.review.domain.repository

import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity

interface ProjectRepository {
    fun save(project: ProjectEntity): ProjectEntity
    fun saveAllUrls(projectUrls: List<ProjectUrlEntity>): List<ProjectUrlEntity>
    fun deleteUrlsByProjectId(projectId: Long)
    fun findProjectByLinkKey(linkKey: String): ProjectEntity?
    fun saveFormLink(formLink: FormLinkEntity): FormLinkEntity
    fun findLinkByLinkKey(linkKey: String) :FormLinkEntity?
}
