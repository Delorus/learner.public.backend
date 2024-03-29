package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Where(clause = "removed = false") //todo add data cleaner scheduling task
@SQLDelete(sql = "update app_user set removed = true where id = ?")
@Table(indexes = [Index(columnList = "email", unique = true)])
open class AppUser {

    @Id
    @GeneratedValue
    open var id: Int = 0

    @Column(unique = true)
    @NaturalId
    open var email: String? = null

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    open lateinit var groups: MutableList<CardGroup>

    @CreationTimestamp
    open lateinit var creationDate: Instant

    @UpdateTimestamp
    open lateinit var updateDate: Instant

    @Column
    open var removed: Boolean = false

    fun addCardGroup(cardGroup: CardGroup) {
        groups.add(cardGroup)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppUser

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email?.hashCode() ?: 0
    }
}
