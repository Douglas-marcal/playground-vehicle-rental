package playground.vehiclerental.customer.infrastructure.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import playground.vehiclerental.customer.application.CustomerService
import playground.vehiclerental.customer.application.LicenseData
import playground.vehiclerental.customer.application.RegisterCustomerCommand
import playground.vehiclerental.customer.domain.Customer
import playground.vehiclerental.customer.domain.LicenseCategory
import java.time.LocalDate

data class LicenseRequest(
    val number: String,
    val category: LicenseCategory,
    val expiresAt: LocalDate,
) {
    fun toData() = LicenseData(number, category, expiresAt)
}

data class RegisterCustomerRequest(
    val name: String,
    val cpf: String,
    val birthDate: LocalDate,
    val email: String? = null,
    val phone: String? = null,
    val license: LicenseRequest,
)

data class LicenseResponse(
    val number: String,
    val category: LicenseCategory,
    val expiresAt: LocalDate,
)

data class CustomerResponse(
    val id: Long,
    val name: String,
    val cpf: String,
    val birthDate: LocalDate,
    val email: String?,
    val phone: String?,
    val license: LicenseResponse,
) {
    companion object {
        fun from(customer: Customer) = CustomerResponse(
            id = requireNotNull(customer.id),
            name = customer.name,
            cpf = customer.cpf.value,
            birthDate = customer.birthDate,
            email = customer.email,
            phone = customer.phone,
            license = LicenseResponse(customer.license.number, customer.license.category, customer.license.expiresAt),
        )
    }
}

@RestController
@RequestMapping("/customers")
class CustomerController(private val service: CustomerService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: RegisterCustomerRequest): CustomerResponse =
        CustomerResponse.from(
            service.register(
                RegisterCustomerCommand(
                    name = request.name,
                    cpf = request.cpf,
                    birthDate = request.birthDate,
                    email = request.email,
                    phone = request.phone,
                    license = request.license.toData(),
                )
            )
        )

    @GetMapping
    fun list(): List<CustomerResponse> = service.list().map(CustomerResponse::from)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): CustomerResponse = CustomerResponse.from(service.get(id))

    @PutMapping("/{id}/license")
    fun renewLicense(@PathVariable id: Long, @RequestBody request: LicenseRequest): CustomerResponse =
        CustomerResponse.from(service.renewLicense(id, request.toData()))
}
