package ru.sherb.igorprj

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class IgorprjApplication

fun main(args: Array<String>) {
    runApplication<IgorprjApplication>(*args)
}
