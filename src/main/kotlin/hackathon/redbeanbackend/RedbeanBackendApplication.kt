package hackathon.redbeanbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class RedbeanBackendApplication

fun main(args: Array<String>) {
    runApplication<RedbeanBackendApplication>(*args)
}
