package team5.backoffice.domain.exception

data class RecentlyUsedPasswordException(
    override val message: String? = "New Password is used recently"
) : RuntimeException()