package ru.sherb.igorprj.endpoint.userinfo

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sherb.igorprj.endpoint.ResourceNotFoundException
import ru.sherb.igorprj.endpoint.cardgroup.view.CardGroupListView
import ru.sherb.igorprj.persist.entity.AppUser
import ru.sherb.igorprj.persist.repository.AppUserRepository
import ru.sherb.igorprj.persist.repository.CardGroupRepository

/**
 * @author maksim
 * @since 09.09.2019
 */
@RestController
@RequestMapping("v1/user")
class UserInfoEndpoint(
        val userRepository: AppUserRepository,
        val cardGroupRepository: CardGroupRepository
) {

    @GetMapping("{id}/packs")
    @Transactional(readOnly = true)
    fun getUserPacks(@PathVariable id: Int,
                     @RequestParam offset: Int,
                     @RequestParam limit: Int): Page<CardGroupListView> {

        val user = userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException(AppUser::class, id) }

        return cardGroupRepository.findByUser(user, PageRequest.of(offset, limit))
                .map(::CardGroupListView)
    }
}