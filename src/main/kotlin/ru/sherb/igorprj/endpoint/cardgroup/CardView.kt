package ru.sherb.igorprj.endpoint.cardgroup

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.sherb.igorprj.endpoint.card.CardEndpoint
import ru.sherb.igorprj.persist.entity.Answer
import ru.sherb.igorprj.persist.entity.Card
import ru.sherb.igorprj.persist.entity.ExternalCard
import ru.sherb.igorprj.persist.entity.MultimediaCard
import ru.sherb.igorprj.persist.entity.TextCard
import kotlin.reflect.full.findAnnotation

/**
 * @author maksim
 * @since 31.08.2019
 */
fun cardViewOf(card: Card<*>) = CardView(card).apply {
    when (card) {
        is TextCard -> this.content = card.loadContent()
        is ExternalCard -> this.contentLocation = card.loadContent()
        is MultimediaCard -> this.contentLocation = buildLocationContentPath(card.id)
    }
}

@JsonInclude(Include.NON_NULL)
data class CardView(
        val id: Int,
        val subject: String,
        val orderNum: Int,
        val answers: List<AnswerView>?,
        // send only for text card
        var content: String? = null,
        var contentLocation: String? = null
) {

    constructor(card: Card<*>) : this(
            id = card.id,
            subject = card.subject,
            orderNum = card.orderNum,
            answers = if (card.answers.isNotEmpty()) mapAnswerEntitiesToViews(card.answers) else emptyList()
    )
}

private fun mapAnswerEntitiesToViews(answers: Iterable<Answer>): List<AnswerView> {

    val rightAnswer = answers.single(Answer::isRight)
    return answers.shuffled()
            .asSequence()
            .filter { !it.isRight }
            .take(3)
            .toMutableList()
            .apply { add(rightAnswer) }
            .map(::AnswerView)
}

data class AnswerView(val id: Int, val text: String) {

    constructor(answer: Answer) : this(
            id = answer.id,
            text = answer.text
    )
}

private fun buildLocationContentPath(id: Int): String {
    val root = CardEndpoint::class.findAnnotation<RequestMapping>()!!.value.joinToString("/")
    return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath(root)
            .path("/{id}/content")
            .buildAndExpand(id)
            .toUriString()
}