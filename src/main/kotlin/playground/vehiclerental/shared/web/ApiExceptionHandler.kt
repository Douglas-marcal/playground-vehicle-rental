package playground.vehiclerental.shared.web

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import playground.vehiclerental.vehicle.domain.DuplicatePlateException
import playground.vehiclerental.vehicle.domain.InvalidPlateException
import playground.vehiclerental.vehicle.domain.VehicleNotFoundException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException::class)
    fun notFound(e: VehicleNotFoundException) = problem(HttpStatus.NOT_FOUND, e)

    @ExceptionHandler(DuplicatePlateException::class)
    fun conflict(e: DuplicatePlateException) = problem(HttpStatus.CONFLICT, e)

    @ExceptionHandler(InvalidPlateException::class, IllegalArgumentException::class)
    fun badRequest(e: RuntimeException) = problem(HttpStatus.BAD_REQUEST, e)

    private fun problem(status: HttpStatus, e: Exception) =
        ProblemDetail.forStatusAndDetail(status, e.message ?: status.reasonPhrase)
}
