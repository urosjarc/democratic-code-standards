object seed {
    fun all() {

        val users = listOf(
            User(email = "uros.jarc@solvesall.com", password = "pass", admin = true),
            User(email = "name.surname@solvesall.com", password = "pass", admin = true),
        )

        val languages = listOf(
            Language(name = "C"),
            Language(name = "Kotlin"),
            Language(name = "Typescript"),
            Language(name = "Javascript"),
        )

        val linters = listOf(
            Linter(language = languages[1].id, title = "Ktlin", description = "Modern kotlin linter and formatter"),
            Linter(language = languages[3].id, title = "Eslint", description = "Eslint linter and formatter"),
        )

        val codeStyleRule = listOf(
            LinterRule(title = "If braces", description = "Do you want to have if braces", before = "before0", after = "after0"),
            LinterRule(title = "Whitespaces", description = "Do you want to have whitespaces", before = "before1", after = "after1"),
            LinterRule(title = "End line", description = "Do you want to have end line", before = "before2", after = "after2")
        )

        val userLanguages = listOf(
            UserLanguage(user = users[0].id, language = languages[1].id),
            UserLanguage(user = users[0].id, language = languages[2].id),
            UserLanguage(user = users[0].id, language = languages[3].id),
            UserLanguage(user = users[1].id, language = languages[0].id),
            UserLanguage(user = users[1].id, language = languages[1].id),
            UserLanguage(user = users[1].id, language = languages[2].id),
        )

        val rating = listOf(
            Rating(points = -1, title = "FAIL"),
            Rating(points = 0, title = "NEVTRAL"),
            Rating(points = 1, title = "PASS"),
        )

        val feedback = listOf(
            Feedback(user = users[0].id, comment = "comment0"),
            Feedback(user = users[0].id, comment = "comment1"),
            Feedback(user = users[1].id, comment = "comment0"),
            Feedback(user = users[1].id, comment = "comment1"),
        )

        val votes = listOf(
            Vote(rating = rating[0].id, linterRule = codeStyleRule[0].id, user = users[0].id),
            Vote(rating = rating[1].id, linterRule = codeStyleRule[1].id, user = users[0].id),
            Vote(rating = rating[2].id, linterRule = codeStyleRule[2].id, user = users[0].id),
        )

        db.autocommit {
            it.schema.create(schema = codeStyleSchema)
            it.schema.create(schema = userSchema)

            it.table.create<User>()
            it.table.create<Language>()
            it.table.create<Linter>()
            it.table.create<LinterRule>()
            it.table.create<UserLanguage>()
            it.table.create<Rating>()
            it.table.create<Feedback>()
            it.table.create<Vote>()

            it.table.delete<User>()
            it.table.delete<Language>()
            it.table.delete<Linter>()
            it.table.delete<LinterRule>()
            it.table.delete<UserLanguage>()
            it.table.delete<Rating>()
            it.table.delete<Feedback>()
            it.table.delete<Vote>()

            it.batch.insert(users)
            it.batch.insert(languages)
            it.batch.insert(linters)
            it.batch.insert(codeStyleRule)
            it.batch.insert(userLanguages)
            it.batch.insert(rating)
            it.batch.insert(feedback)
            it.batch.insert(votes)
        }
    }
}
