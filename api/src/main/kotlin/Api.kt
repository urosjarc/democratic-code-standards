import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.urosjarc.dbmessiah.extra.kotlinx.UUIDJS
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.slf4j.event.Level
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

fun ApplicationCall.profil(): Profil {
    val principal = this.principal<JWTPrincipal>()
    val data = principal!!.payload.getClaim(Profil.claim).asString().toString()
    return Profil(user_id = Id(UUID.fromString(data)))
}


/**
 * @OpenAPITag User
 * Providing user related services.
 **/
@Resource("/user")
class UserRoute {
    @Resource("login")
    class Login(val parent: UserRoute)

    @Resource("logout")
    class Logout(val parent: UserRoute)
}

/**
 * @OpenAPITag Language
 * Providing programming language related services.
 **/
@Resource("/language")
class LanguageRoute {
}

/**
 * @OpenAPITag Feedback
 * Providing feedback related services.
 **/
@Resource("/feedback")
class FeedbackRoute

object api {
    @JvmStatic
    fun serve(port: Int = System.getenv("PORT")?.toInt() ?: 8080) {

        embeddedServer(Netty, port = port, host = "0.0.0.0") {
            install(Resources)
            install(Sessions) {
                cookie<UserLoginSession>("user-login-session", SessionStorageMemory())
            }
            install(ContentNegotiation) {
                json(Json {
                    this.serializersModule = SerializersModule {
                        contextual(UUIDJS)
                    }
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

            val jwkProvider = JwkProviderBuilder(ENV.JWT_ISSUER)
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()

            this.install(Authentication) {

                this.jwt {
                    this.realm = ENV.JWT_REALM
                    this.verifier(jwkProvider, ENV.JWT_ISSUER) {
                        this.acceptLeeway(3)
                    }
                    this.validate { credential ->
                        val claim = credential.payload.getClaim(Profil.claim)
                        if (claim != null) {
                            JWTPrincipal(credential.payload)
                        } else {
                            null
                        }
                    }
                    this.challenge { defaultScheme, realm -> this.call.respond(HttpStatusCode.Unauthorized) }
                }
            }

            routing {

                this.static {
                    this.staticBasePackage = "static"
                    this.resources(".")
                }

                /**
                 * Access point for user to login.
                 */
                post<UserRoute.Login> {
                    val userLoginReq = this.call.receive<UserLoginReq>()
                    var user: User? = null

                    db.autocommit {
                        val users = it.query.get(output = User::class, input = userLoginReq) {
                            """
                                ${it.SELECT<User>()}
                                where 1=1
                                    and ${it.column(User::email)} = ${it.input(UserLoginReq::email)}
                                    and ${it.column(User::password)} = ${it.input(UserLoginReq::password)}
                            """.trimIndent()
                        }
                        if (users.size == 1) user = users.firstOrNull()
                    }

                    if (user == null) return@post this.call.respond(HttpStatusCode.Unauthorized)

                    val publicKey = jwkProvider.get(ENV.JWT_KEYID).publicKey
                    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(ENV.JWT_PRIVATE_KEY))
                    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
                    val token = JWT.create()
                        .withAudience(ENV.JWT_AUDIENCE)
                        .withIssuer(ENV.JWT_ISSUER)
                        .withClaim(Profil.claim, user!!.id.toString())
                        .withExpiresAt(Date(System.currentTimeMillis() + 5 * 60 * 1000))
                        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))

                    this.call.respond(PrijavaRes(token = token))
                }
                /**
                 * Getting currently in session user.
                 */
                this.authenticate {

                    get<UserRoute> {
                        val profilRes = this.call.profil()
                        call.respond(profilRes)
                    }
                    /**
                     * Action for user logout.
                     */
                    get<UserRoute.Logout> {

                    }
                    /**
                     * Get list of programming languages.
                     */
                    get<LanguageRoute> {
                        val profil = this.call.profil()
                        var languages: List<Language>? = null

                        db.autocommit {
                            languages = it.query.get(output = Language::class, input = profil) {
                                """
                                    ${it.SELECT<Language>()}
                                    join ${it.table<UserLanguage>()} on
                                        ${it.column(UserLanguage::user)} = ${it.input(Profil::user_id)}
                                """
                            }.distinctBy { it.id }


                        }

                        if (languages != null) {
                            this.call.respond(languages!!)
                        } else this.call.respond(HttpStatusCode.Unauthorized)
                    }
                }
            }

        }.start(wait = true)
    }
}
