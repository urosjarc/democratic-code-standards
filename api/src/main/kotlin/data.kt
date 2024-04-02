import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginReq(
    val email: String,
    val password: String
)

@Serializable
data class UserLoginSession(val id: Id<User>) : Principal

@Serializable
data class Profil(val user_id: Id<User>) {
    companion object {
        val claim: String = Profil::class.simpleName.toString()
    }
}

@Serializable
data class PrijavaRes(val token: String)
