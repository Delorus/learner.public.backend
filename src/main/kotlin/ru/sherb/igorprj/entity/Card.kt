package ru.sherb.igorprj.entity

import org.springframework.data.annotation.Immutable
import javax.persistence.*

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Immutable
data class Card(

        @Id
        @GeneratedValue
        val id: Int,

        @Column(nullable = false)
        val subject: String,

        @Column
        val content: String?,

        @OneToMany
        @JoinColumn(nullable = false)
        val answers: List<Answer>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
