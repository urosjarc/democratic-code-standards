import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@JvmInline
@Serializable
value class Id<T>(@Contextual val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}

@Serializable
data class User(
    val id: Id<User> = Id(),
    val email: String,
    val password: String,
    val admin: Boolean
)

@Serializable
data class Language(
    val id: Id<Language> = Id(),
    val name: String,
)

@Serializable
data class Feedback(
    val id: Id<Feedback> = Id(),
    val user: Id<User>,
    val comment: String,
    val created: Instant = Clock.System.now()
)

@Serializable
data class Linter(
    val id: Id<Linter> = Id(),
    val language: Id<Language> = Id(),
    val title: String,
    val description: String
)

@Serializable
data class LinterRule(
    val id: Id<LinterRule> = Id(),
    val title: String,
    val description: String,
    val before: String,
    val after: String
)

@Serializable
data class UserLanguage(
    val id: Id<UserLanguage> = Id(),
    val user: Id<User>,
    val language: Id<Language>
)

@Serializable
data class Vote(
    val id: Id<Vote> = Id(),
    val rating: Id<Rating>,
    val linterRule: Id<LinterRule>,
    val user: Id<User>,
    val created: Instant = Clock.System.now()
)

@Serializable
data class Rating(
    val id: Id<Rating> = Id(),
    val points: Int,
    val title: String
)
