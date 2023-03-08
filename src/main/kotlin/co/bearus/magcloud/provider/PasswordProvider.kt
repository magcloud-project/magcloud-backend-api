package co.bearus.magcloud.provider

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordProvider(
    private val bCrypt: BCryptPasswordEncoder
) {
    fun encrypt(password: String): String = bCrypt.encode(password)
    fun compare(password: String, encryptedPassword: String): Boolean = bCrypt.matches(password, encryptedPassword)
}
