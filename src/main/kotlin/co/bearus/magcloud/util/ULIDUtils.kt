package co.bearus.magcloud.util

import com.github.f4b6a3.ulid.UlidCreator

class ULIDUtils {
    companion object {
        fun generate(): String {
            return UlidCreator.getMonotonicUlid().toString()
        }
    }
}
