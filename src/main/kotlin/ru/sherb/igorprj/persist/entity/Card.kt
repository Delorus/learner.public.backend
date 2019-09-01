package ru.sherb.igorprj.persist.entity

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import org.hibernate.engine.jdbc.BlobProxy
import org.springframework.util.unit.DataSize
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.Blob
import java.time.Instant
import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.OneToMany

/**
 * @author maksim
 * @since 26.08.2019
 */
@Entity
@Where(clause = "removed = false")
@SQLDelete(sql = "update card set removed = true where id = ?")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Card<T> {

    @Id
    @GeneratedValue
    open var id: Int = 0

    @Column(nullable = false)
    open var orderNum: Int = 0

    @Column(nullable = false)
    open lateinit var subject: String

    @JoinColumn(nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open lateinit var answers: MutableList<Answer>

    @CreationTimestamp
    open lateinit var creationDate: Instant

    @UpdateTimestamp
    open lateinit var updateDate: Instant

    @Column
    open var removed: Boolean = false

    abstract fun loadContent(): T

    abstract fun saveContent(input: InputStream)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id
}

@Entity
open class TextCard : Card<String>() {

    @Column
    private lateinit var text: String

    override fun loadContent() = text

    override fun saveContent(input: InputStream) {
        InputStreamReader(input, StandardCharsets.UTF_8).use {
            text = it.readText()
        }
    }
}

@Entity
open class MultimediaCard : Card<InputStream>() {

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    private lateinit var data: Blob

    override fun loadContent(): InputStream = data.binaryStream

    override fun saveContent(input: InputStream) {
        data = BlobProxy.generateProxy(input, DataSize.ofMegabytes(10).toBytes())
    }
}

open class ExternalCard : Card<String>() {

    @Column
    private lateinit var link: String

    override fun loadContent() = link

    override fun saveContent(input: InputStream) {
        InputStreamReader(input, StandardCharsets.UTF_8).use {
            link = it.readText()
        }
    }
}