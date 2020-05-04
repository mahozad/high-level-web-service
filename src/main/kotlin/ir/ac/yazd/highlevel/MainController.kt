package ir.ac.yazd.highlevel

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.time.Duration
import java.time.Instant
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class MainController {

    private val startTime = Instant.now()
    private var clientNumber = 0
    private val data = mutableMapOf<Int, MutableSet<Pair<String, String>>>()

    @GetMapping("/uptime")
    @ResponseBody
    fun uptime(): String {
        return "${Duration.between(startTime, Instant.now()).seconds}s"
    }

    @GetMapping("/data")
    @ResponseBody
    fun getData(@CookieValue userId: Int?): String {
        return if (userId == null || !data.containsKey(userId)) "No data yet" else data.getValue(userId).toString()
    }

    @GetMapping("/store")
    @ResponseBody
    fun storeData(@CookieValue userId: Int?,
                  @RequestParam key: String, @RequestParam value: String,
                  response: HttpServletResponse): String {

        return if (userId == null || !data.containsKey(userId)) {
            clientNumber++
            data[clientNumber] = mutableSetOf(Pair(key, value))

            val cookie = Cookie("userId", clientNumber.toString())
            response.addCookie(cookie)

            data.getValue(clientNumber).toString()
        } else {
            data.getValue(userId).add(Pair(key, value))
            data.getValue(userId).toString()
        }
    }
}
