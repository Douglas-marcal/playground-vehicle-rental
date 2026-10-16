package playground.vehiclerental.shared.domain

/** Recurso inexistente (HTTP 404). */
open class NotFoundException(message: String) : RuntimeException(message)

/** Conflito com o estado atual, como dado duplicado (HTTP 409). */
open class ConflictException(message: String) : RuntimeException(message)

/** Dado com formato inválido (HTTP 400). */
open class ValidationException(message: String) : RuntimeException(message)

/** Regra de negócio violada por um dado bem formado (HTTP 422). */
open class BusinessRuleException(message: String) : RuntimeException(message)
