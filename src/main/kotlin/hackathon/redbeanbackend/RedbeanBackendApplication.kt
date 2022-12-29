package hackathon.redbeanbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedbeanBackendApplication

fun main(args: Array<String>) {
	runApplication<RedbeanBackendApplication>(*args)
}
