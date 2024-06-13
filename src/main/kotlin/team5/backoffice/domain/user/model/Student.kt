package team5.backoffice.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "student") //todo
class Student(

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = true)
    var password: String,

    @Column(name = "provider_name", nullable = true)
    val providerName: String? = null,

    @Column(name = "provider_id", nullable = true)
    val providerId: String? = null,

    @Column(name = "prev_passwords", nullable = true)
    val prevPasswords: String? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}