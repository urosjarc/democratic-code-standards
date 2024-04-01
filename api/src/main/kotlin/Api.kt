import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

@Resource("/user")
class UserRoute {
    @Resource("login")
    class Login(val parent: UserRoute, val password: String, val email: String)

    @Resource("logout")
    class Logout(val parent: UserRoute)

    @Resource("feedback")
    class Feedback(val parent: UserRoute) {

    }
}

@Resource("/language")
class LanguageRoute(val parent: UserRoute) {
    @Resource("{id}")
    class IdRoute(val parent: UserRoute, val id: Id<Language>) {
        @Resource("/linter")
        class Linter(val parent: UserRoute) {

            @Resource("/rule")
            class Rule(val parent: Linter) {

            }
        }
    }
}

object api {
    @JvmStatic
    fun serve(port: Int = System.getenv("PORT")?.toInt() ?: 8080) {

        embeddedServer(Netty, port = port, host = "0.0.0.0") {
            install(Resources)
            install(ContentNegotiation) {
                json(Json {
                    allowSpecialFloatingPointValues = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(CORS) {
                this.allowHeader(HttpHeaders.ContentType)
                this.allowHeader(HttpHeaders.Authorization)
                this.anyHost()
                this.allowMethod(HttpMethod.Post)
                this.allowMethod(HttpMethod.Put)
                this.allowMethod(HttpMethod.Get)
                this.allowMethod(HttpMethod.Delete)
            }
            this.install(CallLogging) {
                this.level = Level.DEBUG
                format { call ->
                    val status = call.response.status()
                    val httpMethod = call.request.httpMethod.value
                    val userAgent = call.request.headers["User-Agent"]
                    "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
                }
            }

            routing {
                /**
                 * USER
                 */
                post<UserRoute.Login> {

                }
                get<UserRoute.Logout> {

                }
                get<UserRoute.Feedback> {

                }
                post<UserRoute.Feedback> {

                }
                /**
                 * LANGUAGE
                 */
                get<LanguageRoute> {

                }
            }

        }.start(wait = true)
    }
}
