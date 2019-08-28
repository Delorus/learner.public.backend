package ru.sherb.igorprj.endpoint

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author maksim
 * @since 27.08.2019
 */
@RestController
@RequestMapping(path = ["/v1"])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EndpointVersion1