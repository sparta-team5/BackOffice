package team5.backoffice.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "student") //todo
class Student(

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = true)
    var password: String,

    @Column(name = "provider_name", nullable = true)
    val providerName: String? = null,

    @Column(name = "provider_id", nullable = true)
    val providerId: String? = null,

    @Column(name = "old_password1", nullable = true)
    var oldPassword1: String? = null,

    @Column(name = "old_password2", nullable = true)
    var oldPassword2: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun changePassword(newPassword: String) {
        oldPassword2 = oldPassword1
        oldPassword1 = password
        password = newPassword
    }

    fun isNewPasswordValid(newPassword: String): Boolean {
        return newPassword != oldPassword1 && newPassword != oldPassword2 && newPassword != password
    }


}

