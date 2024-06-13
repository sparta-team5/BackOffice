package team5.backoffice.domain.auth.jwt

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BCHash {
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, hashedPassword)
    }
}