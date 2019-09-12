package ru.sherb.igorprj.search

import org.hibernate.search.jpa.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sherb.igorprj.persist.entity.CardGroup
import java.util.concurrent.TimeUnit
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author maksim
 * @since 08.09.2019
 */
@Repository
@Transactional(readOnly = true)
class CardGroupSearchService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun search(text: String, pageable: Pageable): Page<CardGroup> {
        val manager = Search.getFullTextEntityManager(entityManager)
        val builder = manager.searchFactory
                .buildQueryBuilder()
                .forEntity(CardGroup::class.java)
                .get()

        val query = builder.keyword()
                .onFields("topic", "cards.subject"/*, "cards.text"*/)
                .matching(text)
                .createQuery()

        val result = manager.createFullTextQuery(query, CardGroup::class.java)
                .limitExecutionTimeTo(3, TimeUnit.SECONDS)
                .setFirstResult(pageable.offset.toInt())
                .setMaxResults(pageable.pageSize + 1)
                .resultList as List<CardGroup>

        return PageImpl(result.subList(0, result.size.coerceAtMost(pageable.pageSize)), pageable, pageable.offset + result.size)
    }
}