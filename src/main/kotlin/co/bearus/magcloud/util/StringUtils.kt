package co.bearus.magcloud.util

class StringUtils {
    companion object {
        fun extractULIDFromString(text: String): List<String> {
            val regex = Regex("([@]\\S+)")
            val result = mutableListOf<String>()

            regex.findAll(text).forEach { match ->
                result.add(match.value.substring(1))
            }
            return result.filter { ULIDUtils.isULID(it) }
        }
    }
}
