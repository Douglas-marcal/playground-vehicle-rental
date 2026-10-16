package playground.vehiclerental.customer.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import playground.vehiclerental.customer.domain.Cpf
import playground.vehiclerental.customer.domain.Customer
import playground.vehiclerental.customer.domain.DriverLicense
import playground.vehiclerental.customer.domain.LicenseCategory
import java.time.LocalDate

@Entity
@Table(name = "customer")
class CustomerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 120)
    var name: String,

    @Column(nullable = false, length = 11, unique = true)
    var cpf: String,

    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,

    @Column(length = 160)
    var email: String?,

    @Column(length = 20)
    var phone: String?,

    @Column(name = "cnh_number", nullable = false, length = 11, unique = true)
    var licenseNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "cnh_category", nullable = false, length = 3)
    var licenseCategory: LicenseCategory,

    @Column(name = "cnh_expires_at", nullable = false)
    var licenseExpiresAt: LocalDate,
) {
    fun toDomain() = Customer(
        id = id,
        name = name,
        cpf = Cpf.of(cpf),
        birthDate = birthDate,
        email = email,
        phone = phone,
        license = DriverLicense.of(licenseNumber, licenseCategory, licenseExpiresAt),
    )

    companion object {
        fun from(customer: Customer) = CustomerEntity(
            id = customer.id,
            name = customer.name,
            cpf = customer.cpf.value,
            birthDate = customer.birthDate,
            email = customer.email,
            phone = customer.phone,
            licenseNumber = customer.license.number,
            licenseCategory = customer.license.category,
            licenseExpiresAt = customer.license.expiresAt,
        )
    }
}
