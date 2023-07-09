package co.bearus.magcloud.domain.service.user

import co.bearus.magcloud.controller.dto.response.FileUploadResponse
import co.bearus.magcloud.domain.exception.UserNotFoundException
import co.bearus.magcloud.domain.exception.ValidationException
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.util.ULIDUtils
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration

@Service
class UserProfileImageService(
    @Value("\${app.aws.bucket-name}") private val bucketName: String,
    @Value("\${app.aws.upload-expiration}") private val uploadExpiration: Long,
    private val s3Presigner: S3Presigner,
    private val userRepository: JPAUserRepository,
) {
    fun requestUploadUrl(): FileUploadResponse {
        val key = ULIDUtils.generate()
        val actualKey = "profile-images/$key"
        val putRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(actualKey)
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMillis(uploadExpiration))
            .putObjectRequest(putRequest)
            .build()

        return FileUploadResponse(
            uploadUrl = s3Presigner
                .presignPutObject(presignRequest)
                .url().toExternalForm(),
            downloadUrl = URL("https://$bucketName.s3.amazonaws.com/$actualKey").toExternalForm()
        )
    }

    @Transactional
    fun changeProfileImage(userId: String, imageUrl: String) {
        if (!imageUrl.startsWith("https://$bucketName.s3.amazonaws.com")) throw ValidationException()
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        user.profileImageUrl = imageUrl
        userRepository.save(user)
    }
}
