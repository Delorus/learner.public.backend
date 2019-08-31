package ru.sherb.igorprj.endpoint.cardgroup

/**
 * @author maksim
 * @since 28.08.2019
 */
data class NewCard(
        val subject: String,
        val contentType: ContentType,
        val answers: List<NewAnswer>?
)

enum class ContentType {
    TEXT, BINARY, EXTERNAL
}

data class NewAnswer(val text: String, val isRight: Boolean) //todo validate that only one right answer
