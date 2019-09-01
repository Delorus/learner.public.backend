package ru.sherb.igorprj.endpoint.card

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sherb.igorprj.endpoint.ResourceNotFoundException
import ru.sherb.igorprj.persist.entity.Card
import ru.sherb.igorprj.persist.entity.ExternalCard
import ru.sherb.igorprj.persist.entity.MultimediaCard
import ru.sherb.igorprj.persist.entity.TextCard
import ru.sherb.igorprj.persist.repository.CardRepository
import java.net.URI
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletResponse

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
        val card = cardRepository.findById(id)
                .orElseThrow { ResourceNotFoundException(Card::class, id) }

        card.subject = subject

        return ResponseEntity.ok().build()
    }

    @GetMapping("{id}/content")
    @Transactional(readOnly = true)
    fun loadContent(@PathVariable id: Int, response: HttpServletResponse): ResponseEntity<Any> {
        val card = cardRepository.findById(id)
                .orElseThrow { ResourceNotFoundException(Card::class, id) }

        //todo rethink card and content typing
        return when (card) {
            is TextCard -> ResponseEntity.ok()
                    .contentType(MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(card.loadContent())
            is ExternalCard -> ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create(card.loadContent()))
                    .body(card.loadContent())
            is MultimediaCard -> {
                response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                card.loadContent().transferTo(response.outputStream) //fixme i hate spring
                ResponseEntity.ok().build<Any>()
            }
        }
    }
}