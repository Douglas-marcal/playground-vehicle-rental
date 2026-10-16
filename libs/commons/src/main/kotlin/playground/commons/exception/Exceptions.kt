package playground.commons.exception

/**
 * Root of all business exceptions. A generic error handler, such as an API exception handler,
 * can handle this class and let technical failures pass through.
 */
abstract class ApplicationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/** The requested resource does not exist. Usually mapped to HTTP 404. */
open class NotFoundException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)

/** The request conflicts with the current state, such as a value that must be unique. Usually mapped to HTTP 409. */
open class ConflictException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)

/** The received data has an invalid format. Usually mapped to HTTP 400. */
open class ValidationException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)

/** The data is valid but violates a business rule. Usually mapped to HTTP 422. */
open class BusinessRuleException(message: String, cause: Throwable? = null) : ApplicationException(message, cause)
