package taeyun.malanalter.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

val logger = KotlinLogging.logger{}
@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        // 서버에러의 경우 추적할 수 있는 uuid 와 함께 root cause 와 함께
        if (ex is AlerterServerError) {
            logger.error { "[${ex.uuid}] Server Error occur : ${ex.rootCause?.message?:ex.message}" }
        }
        val errorResponse = ErrorResponse.of(ex)
        return ResponseEntity.status(errorResponse.status).body(errorResponse)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleServerError(ex: RuntimeException):ResponseEntity<ErrorResponse>{
        logger.error { "Internal Server Error with ${ex.message}" }
        val errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

}