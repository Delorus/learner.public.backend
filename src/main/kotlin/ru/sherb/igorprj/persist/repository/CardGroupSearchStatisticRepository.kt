package ru.sherb.igorprj.persist.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sherb.igorprj.persist.entity.CardGroupSearchStatistic

/**
 * @author maksim
 * @since 29.08.2019
 */
@Repository
interface CardGroupSearchStatisticRepository : JpaRepository<CardGroupSearchStatistic, Int> {

    @Modifying
    @Query("""
        update card_group_search_statistic
        set number_of_request=number_of_request+1 
        where query_text=:queryText""", nativeQuery = true)
    fun incNumOfQueryRequest(queryText: String): Int
}