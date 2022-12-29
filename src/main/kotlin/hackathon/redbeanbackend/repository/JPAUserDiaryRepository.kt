package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryRepository : JpaRepository<UserDiaryEntity, Long>
