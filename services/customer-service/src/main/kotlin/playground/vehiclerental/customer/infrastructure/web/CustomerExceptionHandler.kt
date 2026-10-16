package playground.vehiclerental.customer.infrastructure.web

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import playground.commons.exception.BusinessRuleException
import playground.commons.exception.ConflictException
import playground.commons.exception.NotFoundException
import playground.commons.exception.ValidationException

@RestControllerAdvice
class CustomerExceptionHandler {

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
