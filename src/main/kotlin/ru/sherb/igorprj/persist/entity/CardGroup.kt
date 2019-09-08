package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.IndexedEmbedded
import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OrderBy

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Indexed
@Where(clause = "removed = false")
@SQLDelete(sql = "update card_group set removed = true where id = ?")
open class CardGroup {

    @Id
    @GeneratedValue
    open var id: Int = 0

    @Field
    @Column
    open var topic: String? = null

    @Column
    @ElementCollection(targetClass = String::class)
    open var tags: MutableSet<String>? = null

    @Column
    open var isPrivate: Boolean = true

    // not null, but there are some cards in the "building" status
    // todo clean building cards after some time
    @IndexedEmbedded
    @JoinColumn
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderBy("orderNum")
    open lateinit var cards: MutableList<Card<*>>

    @Column
    open var cardsCount: Int = 0

    @CreationTimestamp
    open lateinit var creationDate: Instant

    @UpdateTimestamp
    open lateinit var updateDate: Instant

    @Column
    open var removed: Boolean = false

    fun addCard(card: Card<*>) {
        cards.add(card)
        cardsCount++
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
