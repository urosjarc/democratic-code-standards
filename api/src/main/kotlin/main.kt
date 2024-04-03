import java.io.File

fun main() {
    File("db.plantuml").writeText(serializer.plantUML(withOtherColumns = false))
    seed.all()
    api.serve()
}
