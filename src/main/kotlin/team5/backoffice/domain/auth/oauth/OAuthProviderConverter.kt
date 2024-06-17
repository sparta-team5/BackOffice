package team5.backoffice.domain.auth.oauth

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import team5.backoffice.domain.auth.oauth.type.OAuthProvider

@Component
class OAuthProviderConverter : Converter<String, OAuthProvider> {
    override fun convert(source: String): OAuthProvider {
        return runCatching {
            OAuthProvider.valueOf(source.uppercase())
        }.getOrElse {
            throw IllegalArgumentException("Provider not found")
        }
    }
}
