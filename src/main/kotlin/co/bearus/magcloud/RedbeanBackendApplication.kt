package co.bearus.magcloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
class RedbeanBackendApplication

fun main(args: Array<String>) {
    runApplication<RedbeanBackendApplication>(*args)
}
