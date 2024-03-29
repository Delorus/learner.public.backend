package ru.sherb.igorprj.endpoint.cardgroup

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.sherb.igorprj.endpoint.PageView
import ru.sherb.igorprj.endpoint.ResourceNotFoundException
import ru.sherb.igorprj.endpoint.UserNotFoundException
import ru.sherb.igorprj.endpoint.cardgroup.view.CardGroupListView
import ru.sherb.igorprj.endpoint.cardgroup.view.CardGroupView
import ru.sherb.igorprj.endpoint.cardgroup.view.CardView
import ru.sherb.igorprj.endpoint.cardgroup.view.ContentType
import ru.sherb.igorprj.endpoint.cardgroup.view.NewAnswer
import ru.sherb.igorprj.endpoint.cardgroup.view.NewCard
import ru.sherb.igorprj.endpoint.cardgroup.view.NewCardGroup
import ru.sherb.igorprj.endpoint.cardgroup.view.cardViewOf
import ru.sherb.igorprj.endpoint.emptyPageView
import ru.sherb.igorprj.persist.entity.Answer
import ru.sherb.igorprj.persist.entity.AppUser
import ru.sherb.igorprj.persist.entity.Card
import ru.sherb.igorprj.persist.entity.CardGroup
import ru.sherb.igorprj.persist.entity.CardGroupSearchStatistic
import ru.sherb.igorprj.persist.entity.ExternalCard
import ru.sherb.igorprj.persist.entity.MultimediaCard
import ru.sherb.igorprj.persist.entity.TextCard
import ru.sherb.igorprj.persist.repository.AnswerRepository
import ru.sherb.igorprj.persist.repository.AppUserRepository
import ru.sherb.igorprj.persist.repository.CardGroupRepository
import ru.sherb.igorprj.persist.repository.CardGroupSearchStatisticRepository
import ru.sherb.igorprj.persist.repository.CardRepository
import ru.sherb.igorprj.search.CardGroupSearchService
import java.security.Principal

/**
 * @author maksim
 * @since 27.08.2019
 */
@RestController
@RequestMapping("v1/packs", consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
class CardGroupEndpoint(
        val cardGroupSearchStatisticRepository: CardGroupSearchStatisticRepository,
        val cardGroupRepository: CardGroupRepository,
        val answerRepository: AnswerRepository,
        val cardRepository: CardRepository,
        val cardGroupSearch: CardGroupSearchService,
        val userRepository: AppUserRepository
) {

    private val log = LoggerFactory.getLogger(CardGroupEndpoint::class.java)

    @GetMapping
    @Transactional
    fun getPage(@RequestParam(required = false) query: String?,
                @RequestParam(required = false, defaultValue = "0") offset: Int,
                @RequestParam(required = false, defaultValue = "10") limit: Int): PageView<CardGroupListView> {

        query?.also {
            val updated = cardGroupSearchStatisticRepository.incNumOfQueryRequest(it)
            if (updated == 0) {
                cardGroupSearchStatisticRepository.save(CardGroupSearchStatistic().apply {
                    queryText = it
                })
            }
        }

        return try {
            if (query != null) {
                PageView(cardGroupSearch.search(query, PageRequest.of(offset, limit)).map(::CardGroupListView))
            } else {
                PageView(cardGroupRepository.findAll(PageRequest.of(offset, limit)).map(::CardGroupListView))
            }
        } catch (e: Exception) {
            log.error(e.message, e)
            emptyPageView()
        }
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Int,
                @RequestParam(required = false, defaultValue = "4") fetchCard: Int): CardGroupView
            = cardGroupRepository.findById(id) //fixme inefficient loading from db (one request = one card instead of one per all)
                .map { CardGroupView(it, fetchCard) }
                .orElseThrow { ResourceNotFoundException(CardGroup::class, id) }

    //todo to public card group endpoint

    //todo bind to current user (get from credentials)
    @PostMapping
    @Transactional
    fun create(principal: Principal, @RequestBody newCardGroup: NewCardGroup): ResponseEntity<Any> {
        val user = userRepository.findByEmail(principal.name)
                .orElseThrow { UserNotFoundException(principal.name) }

        val cardGroup = createCardGroup(user, newCardGroup)

        return ResponseEntity.created(buildLocation(cardGroup.id)).build()
    }

    @Transactional
    @PostMapping("{id}/cards", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addCard(@PathVariable id: Int, newCard: NewCard, content: MultipartFile): ResponseEntity<Any> {
        val maybeCardGroup = cardGroupRepository.findById(id)

        if (maybeCardGroup.isEmpty) {
            throw ResourceNotFoundException(CardGroup::class, id)
        }

        val card = createCard(maybeCardGroup.get(), newCard)
        card.saveContent(content.inputStream)

        return ResponseEntity.created(buildLocation(card.id)).build()
    }

    @GetMapping("{id}/cards")
    fun getCards(@PathVariable id: Int,
                 @RequestParam(required = false, defaultValue = "0") offset: Int,
                 @RequestParam(required = false, defaultValue = "10") limit: Int): PageView<CardView> {

        val maybeCardGroup = cardGroupRepository.findById(id)

        if (maybeCardGroup.isEmpty) {
            throw ResourceNotFoundException(CardGroup::class, id)
        }

        return try {
            val page = cardRepository.findAll(PageRequest.of(offset, limit)).map(::cardViewOf)
            PageView(page)
        } catch (e: Exception) {
            log.error(e.message, e)
            emptyPageView()
        }
    }

    private fun createCard(group: CardGroup, newCard: NewCard): Card<*> {
        val initCard: Card<*>.() -> Unit = {
            subject = newCard.subject
            answers = if (newCard.answers != null) createListOfAnswers(this, newCard.answers) else mutableListOf()
            orderNum = group.cardsCount
        }

        val card = when (newCard.contentType) {
            ContentType.BINARY -> MultimediaCard().apply(initCard)
            ContentType.TEXT -> TextCard().apply(initCard)
            ContentType.EXTERNAL -> ExternalCard().apply(initCard)
        }

        return cardRepository.save(card).also(group::addCard)
    }

    private fun createListOfAnswers(parent: Card<*>, answers: List<NewAnswer>) = answers.map {
        Answer().apply {
            text = it.text
            isRight = it.isRight
        }
    }.onEach { answerRepository.save(it) }.onEach { parent.answers.add(it) }.toMutableList()

    private fun createCardGroup(user: AppUser, newCardGroup: NewCardGroup): CardGroup {
        val cardGroup = CardGroup()
        cardGroup.topic = newCardGroup.topic

        user.addCardGroup(cardGroup)

        return cardGroupRepository.save(cardGroup)
    }

    private fun buildLocation(id: Int, addPath: String = "") =
            ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}/$addPath")
                    .buildAndExpand(id)
                    .toUri()
}