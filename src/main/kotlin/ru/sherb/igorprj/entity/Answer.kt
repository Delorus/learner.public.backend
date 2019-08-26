package ru.sherb.igorprj.entity

import org.springframework.data.annotation.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Immutable
data class Answer(

        @Id
        @GeneratedValue
        val id: Int,

        @NotBlank
        @Column(nullable = false)
        val text: String,

        @Column(nullable = false)
        val isRight: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Answer

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
