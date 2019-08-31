package ru.sherb.igorprj.endpoint.cardgroup

import ru.sherb.igorprj.persist.entity.CardGroup

/**
 * @author maksim
 * @since 31.08.2019
 */
data class CardGroupListView(
        val id: Int,
        val topic: String?
        //todo tags
) {

    constructor(group: CardGroup) : this(
            id = group.id,
            topic = group.topic
    )
}

data class CardGroupView(
        val id: Int,
        val topic: String?,
        //todo val tags
        val cards: List<CardView>,
        val cardsCount: Int
) {

    constructor(group: CardGroup, cardsLimit: Int) : this(
            id = group.id,
            topic = group.topic,
            cardsCount = group.cardsCount,
            cards = fetchCardsFrom(group, cardsLimit)
    )

}

private fun fetchCardsFrom(group: CardGroup, cardsLimit: Int) = group.cards
        .take(cardsLimit)
        .map(::CardView)
