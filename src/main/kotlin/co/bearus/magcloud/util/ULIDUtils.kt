package co.bearus.magcloud.util

import com.github.f4b6a3.ulid.UlidCreator

class ULIDUtils {
    companion object {
        fun generate(): String {
            return UlidCreator.getMonotonicUlid().toString()
        }

        fun isULID(input: String): Boolean {
            val ulidRegex = Regex("^[0-9a-z]{26}$")
            return ulidRegex.matches(input)
        }
    }
}
