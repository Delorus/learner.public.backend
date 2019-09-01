package ru.sherb.igorprj.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author maksim
 * @since 01.09.2019
 */
@Component
class SpringContext : ApplicationContextAware {

    companion object {
        lateinit var context: ApplicationContext

        inline fun <reified T> getBean(): T {
            return context.getBean(T::class.java)
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

}