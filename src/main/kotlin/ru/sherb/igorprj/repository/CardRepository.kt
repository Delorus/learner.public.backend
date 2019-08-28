package ru.sherb.igorprj.repository

import org.springframework.data.repository.PagingAndSortingRepository
import ru.sherb.igorprj.entity.Card

/**
 * @author maksim
 * @since 28.08.2019
 */
interface CardRepository : PagingAndSortingRepository<Card, Int>