package ac.dnd.server.review.infrastructure.persistence.mapper

import ac.dnd.server.review.domain.model.FormLink
import ac.dnd.server.review.domain.model.Project
import ac.dnd.server.review.domain.model.ProjectUrl
import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
import org.springframework.stereotype.Component

@Component
class ProjectPersistenceMapper {

    fun toDomain(entity: ProjectEntity): Project {
        return Project(
            id = entity.id!!,
            generationInfo = entity.generationInfo,
            name = entity.name,
            description = entity.description,
            techStacks = entity.techStacks,
            fileId = entity.fileId,
            status = entity.status,
            submittedAt = entity.submittedAt
        )
    }

    fun toEntity(domain: Project): ProjectEntity {
        return ProjectEntity(
            generationInfo = domain.generationInfo,
            name = domain.name,
            description = domain.description,
            techStacks = domain.techStacks,
            fileId = domain.fileId,
            status = domain.status,
            submittedAt = domain.submittedAt
        )
    }

    fun toDomain(entity: FormLinkEntity): FormLink {
        return FormLink(
            id = entity.id!!,
            linkType = entity.linkType,
            key = entity.key,
            targetId = entity.targetId,
            expired = entity.expired,
            expirationDateTime = entity.expirationDateTime
        )
    }

    fun toEntity(domain: FormLink): FormLinkEntity {
        return FormLinkEntity(
            linkType = domain.linkType,
            key = domain.key,
            targetId = domain.targetId,
            expired = domain.expired,
            expirationDateTime = domain.expirationDateTime
        )
    }

    fun toDomain(entity: ProjectUrlEntity): ProjectUrl {
        return ProjectUrl(
            id = entity.id!!,
            type = entity.type,
            link = entity.link,
            order = entity.order
        )
    }
}
