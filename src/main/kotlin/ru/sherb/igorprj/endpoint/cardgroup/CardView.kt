package ru.sherb.igorprj.endpoint.cardgroup

import ru.sherb.igorprj.persist.entity.Answer
import ru.sherb.igorprj.persist.entity.Card

/**
 * @author maksim
 * @since 31.08.2019
 */
data class CardView(
        val id: Int,
        val subject: String,
        val answers: List<AnswerView>?,
        // send only for text card
        var content: String? = null,
        var contentLocation: String? = null
) {

    constructor(card: Card<*>) : this(
            id = card.id,
            subject = card.subject,
            answers = mapAnswerEntitiesToViews(card.answers)
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
