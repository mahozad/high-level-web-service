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
import java.util.concurrent.atomic.AtomicInteger
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

typealias Id = Int
typealias Params = Map<String, String>
typealias UserData = MutableSet<Map.Entry<String, String>>

fun main() {
    runApplication<HighLevelService>()
}

@SpringBootApplication
@Controller
class HighLevelService {

    private val startTime = Instant.now()
    private var clientNumber = AtomicInteger(0)
    private val data = mutableMapOf<Id, UserData>()

    @GetMapping("/")
    fun home() = "index"

    @GetMapping("/uptime")
    @ResponseBody
    fun uptime() = "${Duration.between(startTime, Instant.now()).seconds}s"

    @GetMapping("/data")
    @ResponseBody
    fun data(@CookieValue userId: Id?) = data[userId]?.toString() ?: "No data yet"

    @GetMapping("/store")
    @ResponseBody
    fun storeData(@CookieValue userId: Id?,
                  @RequestParam params: Params,
                  response: HttpServletResponse): String {
        val id = userId ?: clientNumber.incrementAndGet()
        val isNewUser = userId !in data
        if (isNewUser) addNewUser(id, response)
        for (param in params) data[id]?.add(param)
        return data[id].toString()
    }

    private fun addNewUser(id: Id, response: HttpServletResponse) {
        data[id] = mutableSetOf()
        val cookie = Cookie("userId", id.toString())
        response.addCookie(cookie)
    }
}
