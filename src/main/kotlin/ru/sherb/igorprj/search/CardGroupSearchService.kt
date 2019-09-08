package ru.sherb.igorprj.search

import org.hibernate.search.jpa.Search
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sherb.igorprj.persist.entity.CardGroup
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

    fun search(text: String): List<CardGroup> {
        val manager = Search.getFullTextEntityManager(entityManager)
        val builder = manager.searchFactory
                .buildQueryBuilder()
                .forEntity(CardGroup::class.java)
                .get()

        val query = builder.keyword()
                .onFields("topic", "cards.subject"/*, "cards.text"*/)
                .matching(text)
                .createQuery()

        return manager.createFullTextQuery(query, CardGroup::class.java).resultList as List<CardGroup>
    }
}