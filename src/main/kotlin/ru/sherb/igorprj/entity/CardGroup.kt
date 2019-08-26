package ru.sherb.igorprj.entity

import org.springframework.data.annotation.Immutable
import javax.persistence.*

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Immutable
class CardGroup(

        @Id
        @GeneratedValue
        val id: Int,

        @Column
        val topic: String?,

        @Column
        val tags: HashSet<String>?,

        @Column
        val isPrivate: Boolean = true,

        @OneToMany
        @JoinColumn(nullable = false)
        val cards: List<Card>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
