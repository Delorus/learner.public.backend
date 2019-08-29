package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Where(clause = "removed = false")
@SQLDelete(sql = "update card_group set removed = true where id = ?")
class CardGroup {

    @Id
    @GeneratedValue
    var id: Int = 0

    @Column
    var topic: String? = null

    @Column
    var tags: HashSet<String>? = null

    @Column
    var isPrivate: Boolean = true

    @JoinColumn(nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    var cards: MutableList<Card> = mutableListOf()

    @CreationTimestamp
    val creationDate: Instant = Instant.now()

    @UpdateTimestamp
    var updateDate: Instant = Instant.now()

    @Column
    var removed: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
