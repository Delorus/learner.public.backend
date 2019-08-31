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
        update CardGroupSearchStatistic stat 
        set stat.numberOfRequest=stat.numberOfRequest+1 
        where stat.queryText=:queryText""")
    fun incNumOfQueryRequest(queryText: String): Int
}