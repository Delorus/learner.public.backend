package ru.sherb.igorprj.entity

import org.springframework.data.annotation.Immutable
import javax.persistence.*

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Immutable
class AppUser(

        @Id
        @GeneratedValue
        val id: Int,

        @Column
        val externalId: String,

        @OneToMany
        @JoinColumn
        val groups: List<CardGroup>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppUser

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
