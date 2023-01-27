package co.bearus.magcloud

import co.bearus.magcloud.advice.RequestUserArgumentResolver
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat


@Configuration
class SpringWebConfig(private val resolver: RequestUserArgumentResolver) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(resolver)
    }
    @Bean
    fun objectMapper(): ObjectMapper{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return jacksonObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .setDateFormat(dateFormat)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
}
