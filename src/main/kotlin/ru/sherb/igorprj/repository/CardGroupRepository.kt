package ru.sherb.igorprj.repository

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.sherb.igorprj.entity.CardGroup

/**
 * @author maksim
 * @since 26.08.2019
 */
@Repository
interface CardGroupRepository : PagingAndSortingRepository<CardGroup, Int>
