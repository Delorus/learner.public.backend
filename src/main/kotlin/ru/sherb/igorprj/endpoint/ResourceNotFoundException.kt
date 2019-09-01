package ru.sherb.igorprj.endpoint

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KClass

/**
 * @author maksim
 * @since 28.08.2019
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException : RuntimeException {

    constructor(entity: KClass<*>, id: Int): super("Entity [${entity.simpleName}] with id = $id not found")
}