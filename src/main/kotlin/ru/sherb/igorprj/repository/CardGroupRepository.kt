package ru.sherb.igorprj.repository

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import ru.sherb.igorprj.entity.CardGroup

/**
 * @author maksim
 * @since 26.08.2019
 */
@RepositoryRestResource(path = "v1/groups", collectionResourceRel = "groups")
interface CardGroupRepository : PagingAndSortingRepository<CardGroup, Int>
