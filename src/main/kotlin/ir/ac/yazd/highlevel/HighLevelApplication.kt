package ir.ac.yazd.highlevel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HighLevelApplication

fun main(args: Array<String>) {
	runApplication<HighLevelApplication>(*args)
}
