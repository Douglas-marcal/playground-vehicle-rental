package playground.vehiclerental.shared.web

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import playground.vehiclerental.shared.domain.BusinessRuleException
import playground.vehiclerental.shared.domain.ConflictException
import playground.vehiclerental.shared.domain.NotFoundException
import playground.vehiclerental.shared.domain.ValidationException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException) = problem(HttpStatus.NOT_FOUND, e)

    @ExceptionHandler(ConflictException::class)
    fun conflict(e: ConflictException) = problem(HttpStatus.CONFLICT, e)

    @ExceptionHandler(BusinessRuleException::class)
    fun businessRule(e: BusinessRuleException) = problem(HttpStatus.UNPROCESSABLE_CONTENT, e)

    @ExceptionHandler(ValidationException::class, IllegalArgumentException::class)
    fun badRequest(e: RuntimeException) = problem(HttpStatus.BAD_REQUEST, e)

    private fun problem(status: HttpStatus, e: Exception) =
        ProblemDetail.forStatusAndDetail(status, e.message ?: status.reasonPhrase)
}
