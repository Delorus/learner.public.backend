package ru.sherb.igorprj.entity

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
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
class Answer {

    @Id
    @GeneratedValue
    var id: Int = 0

    @NotBlank
    @Column(nullable = false)
    var text: String = ""

    @Column(nullable = false)
    var isRight: Boolean = false

    @CreationTimestamp
    val creationDate: Instant = Instant.now()

    @UpdateTimestamp
    var updateDate: Instant = Instant.now()

    @Column
    var removed: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Answer

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}
