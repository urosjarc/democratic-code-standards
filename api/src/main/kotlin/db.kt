import com.urosjarc.dbmessiah.domain.C
import com.urosjarc.dbmessiah.domain.Table
import com.urosjarc.dbmessiah.extra.serializers.KotlinxTimeTS
import com.urosjarc.dbmessiah.impl.postgresql.PgSchema
import com.urosjarc.dbmessiah.impl.postgresql.PgSerializer
import com.urosjarc.dbmessiah.impl.postgresql.PgService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.IdTS
import java.util.*
import kotlin.reflect.KProperty1

fun <T : Any> createTable(primaryKey: KProperty1<T, *>): Table<T> {
    //Gather all foreign keys
    val foreignKeys = Table.getInlineTypedForeignKeys(primaryKey = primaryKey)
    return Table(
        primaryKey = primaryKey,
        foreignKeys = foreignKeys,
        constraints = foreignKeys.map { it.first to listOf(C.CASCADE_DELETE) })
}

val codeStyleSchema = PgSchema(
    name = "codeStyle", tables = listOf(
        createTable(Language::id),
        createTable(Linter::id),
        createTable(LinterRule::id),
    )
)
val userSchema = PgSchema(
    name = "user", tables = listOf(
        createTable(UserLanguage::id),
        createTable(Vote::id),
        createTable(User::id),
        createTable(Rating::id),
        createTable(Feedback::id)
    )
)

val serializer = PgSerializer(
    schemas = listOf(
        codeStyleSchema,
        userSchema
    ),
    globalSerializers = BasicTS.postgresql + KotlinxTimeTS.postgresql + listOf(
        IdTS.uuid.postgresql(construct = { Id<Any>(it) }, deconstruct = { it.value })
    ),
    globalInputs = listOf(UserLoginReq::class, Profil::class),
)

val config = Properties().apply {
    this["jdbcUrl"] = "jdbc:postgresql://localhost:5432/public"
    this["username"] = "root"
    this["password"] = "root"
}

val db = PgService(
    config = config,
    ser = serializer
)
