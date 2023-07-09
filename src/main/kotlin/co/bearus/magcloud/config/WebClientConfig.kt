package co.bearus.magcloud.config

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class WebClientConfig(
    private val webClientProperties: WebClientProperties,
) {
    @Bean("kakaoOAuthClient")
    fun kakaoOAuthClient() = buildClient("kakao-oauth")

    @Bean("appleOAuthClient")
    fun appleOAuthClient() = buildClient("apple-oauth")

    @Bean("googleOAuthClient")
    fun googleOAuthClient() = buildClient("google-oauth")

    fun buildClient(clientName: String): WebClient {
        val property = webClientProperties.getClientConfig(clientName) ?: throw RuntimeException()
        return WebClient.builder()
            .baseUrl(property.baseUrl)
            .addHeaderIfExists(property)
            .build()
    }

    private fun WebClient.Builder.addHeaderIfExists(clientConfig: ClientConfig): WebClient.Builder {
        clientConfig.let { property ->
            property.header?.apply {
                this.forEach { header ->
                    this@addHeaderIfExists.defaultHeaders {
                        it.set(header.key, header.value)
                    }
                }
            }
        }
        return this
    }
}
