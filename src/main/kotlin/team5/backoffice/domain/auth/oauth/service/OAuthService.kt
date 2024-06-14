package team5.backoffice.domain.auth.oauth.service

import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.jwt.JwtPlugin
import team5.backoffice.domain.auth.oauth.NaverOAuthClient
import team5.backoffice.domain.user.model.Student
import team5.backoffice.domain.user.repository.StudentRepository

@Service
class OAuthService(
    private val naverOAuthClient: NaverOAuthClient,
    private val studentRepository: StudentRepository,
    private val jwtPlugin: JwtPlugin

) {

    fun getLoginPage(): String {
        return naverOAuthClient.getLoginPageUrl()
    }

    fun naverLogin(code: String): String {
        val accessToken = naverOAuthClient.getAccessToken(code) //accessTOken받아오기
        val userInfo = naverOAuthClient.getUserInfo(accessToken)

        val student = (studentRepository.findByEmail(userInfo.response.email)
            ?: studentRepository.save( //db에 회원이 없다면 회원가입 진행 후 토큰발급
                Student(
                    nickname = userInfo.response.name,
                    email = userInfo.response.email,
                    password = "",
                    providerName = "NAVER",
                    providerId = userInfo.response.id,
                    // prevPasswords = "",

                )
            ))

        //db에 회원이 있다면 토큰발급
        return jwtPlugin.generateAccessToken(student.nickname, student.email)
    }
}