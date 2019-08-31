package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.time.Instant
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
@Where(clause = "removed = false")
@SQLDelete(sql = "update answer set removed = true where id = ?")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
open class Answer {

    @Id
    @GeneratedValue
    open var id: Int = 0

    @NotBlank
    @NaturalId
    @Column(nullable = false)
    open lateinit var text: String

    @Column(nullable = false)
    open var isRight: Boolean = false

    @CreationTimestamp
    open lateinit var creationDate: Instant

    @UpdateTimestamp
    open lateinit var updateDate: Instant

    @Column
    open var removed: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Answer

        if (text != other.text) return false

        return true
    }

    override fun hashCode() = text.hashCode()
}
