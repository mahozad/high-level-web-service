package ir.ac.yazd.highlevel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.time.Duration
import java.time.Instant
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

fun main() {
    runApplication<HighLevelService>()
}

@SpringBootApplication
@Controller
class HighLevelService {

    private val startTime = Instant.now()
    private var clientNumber = 0
    private val data = mutableMapOf<Int, MutableSet<Pair<String, String>>>()

    @GetMapping("/")
    fun home(): String {
        return "index"
    }

    @GetMapping("/uptime")
    @ResponseBody
    fun uptime(): String {
        return "${Duration.between(startTime, Instant.now()).seconds}s"
    }

    @GetMapping("/data")
    @ResponseBody
    fun getData(@CookieValue userId: Int?): String {
        return if (!data.containsKey(userId)) "No data yet" else data[userId].toString()
    }

    @GetMapping("/store")
    @ResponseBody
    fun storeData(@CookieValue userId: Int?,
                  @RequestParam key: String, @RequestParam value: String,
                  response: HttpServletResponse): String {

        if (!data.containsKey(userId)) {
            clientNumber++
            data[clientNumber] = mutableSetOf()

            val cookie = Cookie("userId", clientNumber.toString())
            response.addCookie(cookie)
        }

        data[userId ?: clientNumber]!!.add(Pair(key, value))
        return data[userId ?: clientNumber].toString()
    }
}
