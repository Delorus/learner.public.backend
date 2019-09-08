package ru.sherb.igorprj.search

import org.hibernate.search.jpa.Search
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author maksim
 * @since 08.09.2019
 */
@Service
@Transactional
class BuildIndexOnStart: ApplicationListener<ApplicationReadyEvent> {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)
        fullTextEntityManager.createIndexer().startAndWait()
    }
}