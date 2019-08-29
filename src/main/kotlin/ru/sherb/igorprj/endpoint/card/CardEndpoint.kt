package ru.sherb.igorprj.endpoint.card

import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sherb.igorprj.endpoint.ResourceNotFoundException
import ru.sherb.igorprj.persist.entity.Card
import ru.sherb.igorprj.persist.repository.CardRepository

/**
 * @author maksim
 * @since 28.08.2019
 */
@RestController
@RequestMapping("v1/cards")
class CardEndpoint(
        val cardRepository: CardRepository
) {

    @Transactional
    @PatchMapping("{id}")
    fun changeSubject(@PathVariable id: Int, subject: String): ResponseEntity<Any> {
        val maybeCard = cardRepository.findById(id)

        if (maybeCard.isEmpty) {
            throw ResourceNotFoundException(Card::class, id)
        }

        val card = maybeCard.get()
        card.subject = subject

        return ResponseEntity.ok().build()
    }
}