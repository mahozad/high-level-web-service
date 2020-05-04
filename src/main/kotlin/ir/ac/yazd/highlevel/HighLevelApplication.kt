package ir.ac.yazd.highlevel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping

fun main(args: Array<String>) {
    runApplication<HighLevelApplication>(*args)
}

@SpringBootApplication
class HighLevelApplication {

    @GetMapping("/")
    fun home(): String {
        return "index"
    }
}
