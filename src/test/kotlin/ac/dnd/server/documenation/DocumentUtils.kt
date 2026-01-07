package ac.dnd.server.documenation

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.snippet.Snippet

object DocumentUtils {
    fun getDocumentRequest(): OperationRequestPreprocessor {
        return preprocessRequest(prettyPrint())
    }

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return preprocessResponse(prettyPrint())
    }

    fun document(
        identifier: String,
        vararg snippets: Snippet
    ): RestDocumentationResultHandler {
        return MockMvcRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            *snippets
        )
    }

    fun document(
        identifier: String,
        resourceDetails: ResourceSnippetParameters,
        vararg snippets: Snippet
    ): RestDocumentationResultHandler {
        return MockMvcRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            resource(resourceDetails),
            *snippets
        )
    }

    fun document(
        identifier: String,
        tag: String,
        summary: String,
        description: String,
        vararg snippets: Snippet
    ): RestDocumentationResultHandler {
        val resourceParams = ResourceSnippetParameters.builder()
            .tag(tag)
            .summary(summary)
            .description(description)
            .build()
        return MockMvcRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            resource(resourceParams),
            *snippets
        )
    }
}