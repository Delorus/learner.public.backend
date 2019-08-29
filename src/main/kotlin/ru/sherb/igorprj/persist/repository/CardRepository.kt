package ru.sherb.igorprj.persist.repository

import org.springframework.data.repository.PagingAndSortingRepository
import ru.sherb.igorprj.persist.entity.Card

/**
 * @author maksim
 * @since 28.08.2019
 */
interface CardRepository : PagingAndSortingRepository<Card, Int>