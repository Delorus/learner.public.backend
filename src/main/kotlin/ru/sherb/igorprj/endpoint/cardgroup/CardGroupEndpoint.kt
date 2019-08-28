package ru.sherb.igorprj.endpoint.cardgroup

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.sherb.igorprj.endpoint.ResourceNotFoundException
import ru.sherb.igorprj.entity.Answer
import ru.sherb.igorprj.entity.Card
import ru.sherb.igorprj.entity.CardGroup
import ru.sherb.igorprj.repository.AnswerRepository
import ru.sherb.igorprj.repository.CardGroupRepository
import ru.sherb.igorprj.repository.CardRepository

/**
 * @author maksim
 * @since 27.08.2019
 */
@RestController
@RequestMapping("v1/packs")
class CardGroupEndpoint(
        val cardGroupRepository: CardGroupRepository,
        val answerRepository: AnswerRepository,
        val cardRepository: CardRepository
) {

    @GetMapping
    fun getPage(@RequestParam(required = false) query: String?, //fixme ignoring query param
                @RequestParam offset: Int,
                @RequestParam limit: Int): Page<CardGroup> {

        return cardGroupRepository.findAll(PageRequest.of(offset, limit))
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Int) = cardGroupRepository.findById(id)

    //todo bind to current user (get from credentials)
    @PostMapping
    fun create(@RequestBody newCardGroup: NewCardGroup): ResponseEntity<Any> {
        val cardGroup = createCardGroup(newCardGroup)

        return ResponseEntity.created(buildLocation("{id}", cardGroup.id)).build()
    }

    @PostMapping("{id}/cards")
    @Transactional
    fun addCard(@PathVariable id: Int, newCard: NewCard): ResponseEntity<Any> {
        val maybeCardGroup = cardGroupRepository.findById(id)

        if (maybeCardGroup.isEmpty) {
            throw ResourceNotFoundException(CardGroup::class, id)
        }

        val card = createCard(maybeCardGroup.get(), newCard)
        return ResponseEntity.created(buildLocation("{id}", card.id)).build()
    }

    @GetMapping("{id}/cards")
    fun getCards(@PathVariable id: Int,
                 @RequestParam offset: Int,
                 @RequestParam limit: Int): ResponseEntity<Page<Card>> {

        val maybeCardGroup = cardGroupRepository.findById(id)

        if (maybeCardGroup.isEmpty) {
            throw ResourceNotFoundException(CardGroup::class, id)
        }

        val page = cardRepository.findAll(PageRequest.of(offset, limit))
        return ResponseEntity.ok(page)
    }

    private fun createCard(group: CardGroup, newCard: NewCard) = Card().apply {
        subject = newCard.subject
        content = newCard.content
        answers = createListOfAnswers(this, newCard.answers) as MutableList<Answer>
    }.also { cardRepository.save(it) }.also { group.cards.add(it) }

    private fun createListOfAnswers(card: Card, answers: List<NewAnswer>) = answers.map {
        Answer().apply {
            text = it.text
            isRight = it.isRight
        }
    }.onEach { answerRepository.save(it) }.onEach { card.answers.add(it) }

    private fun createCardGroup(newCardGroup: NewCardGroup): CardGroup {
        val cardGroup = CardGroup()
        cardGroup.topic = newCardGroup.topic

        return cardGroupRepository.save(cardGroup)
    }

    private fun buildLocation(relPath: String, vararg args: Any) =
            ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path(relPath)
                    .buildAndExpand(args)
                    .toUri()
}