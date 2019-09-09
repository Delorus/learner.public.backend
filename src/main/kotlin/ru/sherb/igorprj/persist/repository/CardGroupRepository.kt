package ru.sherb.igorprj.persist.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.sherb.igorprj.persist.entity.AppUser
import ru.sherb.igorprj.persist.entity.CardGroup

/**
 * @author maksim
 * @since 26.08.2019
 */
@Repository
interface CardGroupRepository : PagingAndSortingRepository<CardGroup, Int> {

    @Query("select pack from AppUser u join u.groups pack where u = :user")
    fun findByUser(user: AppUser, pageable: Pageable): Page<CardGroup>
}
