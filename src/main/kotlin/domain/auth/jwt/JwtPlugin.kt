package domain.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

class JwtPlugin {

    companion object {
        const val ISSUER = "team5.backoffice"
        const val SECRET = ""
        const val ACCESS_TOKEN_EXPIRATION_HOUR: Long = 168
    }

    fun validateToken(token: String): String {
        val key = Keys.hmacShaKeyFor(SECRET.toByteArray(StandardCharsets.UTF_8))
        try {
            val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            return claims.payload["email"].toString()
        } catch (e: Exception) {
            throw JwtException("Invalid JWT token")
        }
    }

    fun generateAccessToken(subject: String, email: String): String {
        return generateToken(subject, email, Duration.ofHours(ACCESS_TOKEN_EXPIRATION_HOUR))
    }

    private fun generateToken(subject: String, email: String, expirationPeriod: Duration?): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("email" to email))
            .build()

        val key = Keys.hmacShaKeyFor(SECRET.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(ISSUER)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }

}