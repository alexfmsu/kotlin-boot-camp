package io.rybalkinsd.kotlinbootcamp.practice.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class ChatController {
    val log = logger()
    val messages: Queue<String> = ConcurrentLinkedQueue()
    val usersOnline: MutableMap<String, String> = ConcurrentHashMap()
    val usersRegistered: MutableMap<String, String> = ConcurrentHashMap()

    @RequestMapping(
            path = ["/register"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun register(@RequestParam("name") name: String, @RequestParam("pass") pass: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        usersRegistered.contains(name) -> ResponseEntity.badRequest().body("Already registered")
        pass.isEmpty() || pass.length < 4 -> ResponseEntity.badRequest().body("Password is too short")
        else -> {
            usersRegistered[name] = pass
            messages += "[$name] registered".also { log.info(it) }
            ResponseEntity.ok("Registered")
        }
    }

    @RequestMapping(
        path = ["/login"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun login(@RequestParam("name") name: String, @RequestParam("pass") pass: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        name !in usersRegistered.keys -> ResponseEntity.badRequest().body("User is not registered")
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in")
        usersRegistered[name] != pass -> ResponseEntity.badRequest().body("Incorrect password")
        else -> {
            usersOnline[name] = name
            messages += "[$name] logged in".also { log.info(it) }
            ResponseEntity.ok("Logged in")
        }
    }

    /**
     *
     * Well formatted sorted list of online users
     *
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
        path = ["online"],
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun online(@RequestParam("name")name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        name !in usersOnline.keys -> ResponseEntity.ok().body("Not logged in")
        usersOnline.isEmpty() -> ResponseEntity.ok().body("No online users")
        else -> {
            ResponseEntity.ok().body(usersOnline.values.toList().sortedBy { it.toLowerCase() }.joinToString("\n"))
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = ["logout"],
            method = [RequestMethod.DELETE],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun delete(@RequestParam("name")name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        name !in usersOnline.keys -> ResponseEntity.badRequest().body("Not logged in")
        else -> {
            messages += "[$name] logged out".also { log.info(it) }
            usersOnline.remove(name)
            ResponseEntity.ok("Logged out")
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = ["say"],
            method = [RequestMethod.POST],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun say(@RequestParam("name")name: String, @RequestParam("msg")msg: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        name !in usersOnline.keys -> ResponseEntity.badRequest().body("Not logged in")
        msg.isEmpty() -> ResponseEntity.badRequest().body("Message is empty")
        else -> {
            messages += "[$name]: $msg".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }
    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = ["chat"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun history(@RequestParam("name")name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        name !in usersOnline.keys -> ResponseEntity.badRequest().body("Not logged")
        messages.isEmpty() -> ResponseEntity.ok().body("No messages")
        else -> {
            ResponseEntity.ok().body(messages.joinToString("\n"))
        }
    }
}
