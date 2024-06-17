package team5.backoffice.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.accessTokenSecret}") private val accessTokenSecret: String,
    @Value("\${auth.jwt.refreshTokenSecret}") private val refreshTokenSecret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
    @Value("\${auth.jwt.refreshTokenExpirationHour}") private val refreshTokenExpirationHour: Long,
) {
    fun validateAccessToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(accessTokenSecret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun validateRefreshToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(refreshTokenSecret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }


    fun generateAccessToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(accessTokenExpirationHour), true)
    }

    fun generateRefreshToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(refreshTokenExpirationHour), false)
    }

    private fun generateToken(
        subject: String,
        email: String,
        role: String,
        expirationPeriod: Duration?,
        isAccessToken: Boolean
    ): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role))
            .add(mapOf("email" to email))
            .build()
        val secret = if (isAccessToken) accessTokenSecret else refreshTokenSecret
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val now = Instant.now()

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()
    }

}