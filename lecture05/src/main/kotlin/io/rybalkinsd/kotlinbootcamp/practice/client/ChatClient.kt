package io.rybalkinsd.kotlinbootcamp.practice.client

import com.kohttp.dsl.httpPost
import com.kohttp.dsl.httpGet

import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Request

object ChatClient {
    // Change to server url
    private const val HOST = "54.224.37.210"
    private const val PORT = 8080

    /**
     * POST /chat/name?name=my_name
     */
    fun login(name: String, pass: String) = httpPost {
        host = HOST
        port = PORT
        path = "/chat/name"
        body {
            form {
                "name" to name
                "pass" to pass
            }
        }
    }

    fun register(name: String, pass: String) = httpPost {
        host = HOST
        port = PORT
        path = "/chat/register"
        body {
            form {
                "name" to name
                "pass" to pass
            }
        }
    }

    /**
     * GET /chat/history
     */
    fun viewHistory(name: String): Response = httpGet {
        host = HOST
        port = PORT
        path = "/chat/chat"
        param {
            "name" to name
        }
    }

    /**
     * POST /chat/say
     *
     * Body: "name=my_name&msg='my_message'"
     */
    fun say(name: String, msg: String): Response = httpPost {
        host = HOST
        port = PORT
        path = "/chat/say"
        body {
            form {
                "name" to name
                "msg" to msg
            }
        }
    }

    /**
     * GET /chat/online
     */
    fun viewOnline(name: String): Response = httpGet {
        host = HOST
        port = PORT
        path = "/chat/online"
        param {
            "name" to name
        }
    }

    /**
     * POST /chat/logout?name=my_name
     */
    fun logout(name: String): Response {
        val request = Request.Builder().apply {
            url("http://$HOST:$PORT/chat/logout?name=$name")
            delete()
        }.build()
        return OkHttpClient.Builder().build().newCall(request).execute()
    }
}
