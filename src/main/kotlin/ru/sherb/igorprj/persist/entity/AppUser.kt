package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.validation.constraints.Email

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Where(clause = "removed = false") //todo add data cleaner scheduling task
@SQLDelete(sql = "update app_user set removed = true where id = ?")
class AppUser {

    @Id
    @GeneratedValue
    var id: Int = 0

    @Email
    @Column
    var email: String? = null

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    var groups: List<CardGroup>? = null

    @CreationTimestamp
    val creationDate: Instant = Instant.now()

    @UpdateTimestamp
    var updateDate: Instant = Instant.now()

    @Column
    var removed: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppUser

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}