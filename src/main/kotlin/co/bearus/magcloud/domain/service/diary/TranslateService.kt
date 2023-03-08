package co.bearus.magcloud.domain.service.diary

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TranslateService(
    @Value("\${secret.naver-client-id}") val id: String,
    @Value("\${secret.naver-client-secret}") val secret: String
) {
    fun translateToEnglish(text: String): String {
        val header = HttpHeaders()
        header.set("X-Naver-Client-Id", id)
        header.set("X-Naver-Client-Secret", secret)

        val responseType: ParameterizedTypeReference<HashMap<String?, Any?>?> =
            object : ParameterizedTypeReference<HashMap<String?, Any?>?>() {}

        val request = RequestEntity
            .post("https://openapi.naver.com/v1/papago/n2mt")
            .headers(header)
            .accept(MediaType.APPLICATION_JSON)
            .body(co.bearus.magcloud.controller.dto.PapagoRequestDTO(text = text))

        val result = RestTemplate().exchange(request, responseType).body
        var linkedMap: LinkedHashMap<String?, Any?> = result?.get("message") as LinkedHashMap<String?, Any?>
        linkedMap = linkedMap["result"] as LinkedHashMap<String?, Any?>
        return linkedMap["translatedText"] as String
    }
}
