package ac.dnd.server.documenation

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*

object DocumentUtils {
    fun getDocumentRequest(): OperationRequestPreprocessor {
        return preprocessRequest(prettyPrint())
    }

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return preprocessResponse(prettyPrint())
    }
}