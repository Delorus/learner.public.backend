package ru.sherb.igorprj.persist.repository

import org.springframework.data.repository.CrudRepository
import ru.sherb.igorprj.persist.entity.AppUser
import java.util.*

/**
 * @author maksim
 * @since 04.09.2019
 */
interface AppUserRepository : CrudRepository<AppUser, Int> {

    fun findByEmail(email: String): Optional<AppUser>
}