object ENV {
    val JWT_REALM: String = System.getenv("JWT_REALM")
    val JWT_AUDIENCE: String = System.getenv("JWT_AUDIENCE")
    val JWT_ISSUER: String = System.getenv("JWT_ISSUER")
    val JWT_PRIVATE_KEY: String = System.getenv("JWT_PRIVATE_KEY")
    val JWT_KEYID: String = System.getenv("JWT_KEYID")

    init {
        println(JWT_REALM)
        println(JWT_AUDIENCE)
        println(JWT_ISSUER)
        println(JWT_PRIVATE_KEY)
        println(JWT_KEYID)
    }
}
