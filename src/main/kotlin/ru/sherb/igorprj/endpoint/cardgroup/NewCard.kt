package ru.sherb.igorprj.endpoint.cardgroup

/**
 * @author maksim
 * @since 28.08.2019
 */
data class NewCard(val subject: String, val content: String?, val answers: List<NewAnswer>)

data class NewAnswer(val text: String, val isRight: Boolean) //todo validate that only one right answer
