package taeyun.malanalter.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import taeyun.malanalter.feignclient.DiscordAlertClient


val logger = KotlinLogging.logger{}

@RestControllerAdvice
class GlobalControllerAdvice(val discordClient: DiscordAlertClient) {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        // 서버에러의 경우 추적할 수 있는 uuid 와 함께 root cause 와 함께
        if (ex is AlerterServerError) {
            logger.error { "[${ex.uuid}] Server Error occur : ${ex.rootCause?.message ?: ex.message}\n 스택 트레이스 ${ex.rootCause?.printStackTrace()}" }
        }
        if (ex.isAlarm == true) {
            discordClient.sendAlarm(ErrorNotification.fromException(ex))
        }
        logger.warn { "[${ex.errorCode.status}]  ${ex.message}" }
        val errorResponse = ErrorResponse.of(ex)
        return ResponseEntity.status(errorResponse.status).body(errorResponse)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleServerError(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error { "Internal Server Error with ${ex.message} \n\n ${ex.printStackTrace()}" }
        discordClient.sendAlarm(ErrorNotification.fromException(ex))
        val errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindError(e: BindException): ResponseEntity<ErrorResponse>{
        val errormsg = e.bindingResult.allErrors[0].defaultMessage
        val errorResponse = ErrorResponse.of(AlerterBadRequest(ErrorCode.BAD_REQUEST, message = errormsg, true))
        return ResponseEntity.status(errorResponse.status).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error { "Unexpected Error: ${ex.message} \n\n ${ex.printStackTrace()}" }
        discordClient.sendAlarm(ErrorNotification.fromException(ex))
        val errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

}