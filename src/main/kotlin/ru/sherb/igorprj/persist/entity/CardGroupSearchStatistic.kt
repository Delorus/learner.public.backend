package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

/**
 * @author maksim
 * @since 29.08.2019
 */
@Entity
@Table(indexes = [Index(columnList = "queryText", unique = true)])
open class CardGroupSearchStatistic{

    @Id
    @GeneratedValue
    var id: Int = 0

    @NaturalId
    @Column(updatable = false, nullable = false)
    var queryText: String = ""

    @Column
    var numberOfRequest: Int = 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardGroupSearchStatistic

        if (queryText != other.queryText) return false

        return true
    }

    override fun hashCode() = queryText.hashCode()
}