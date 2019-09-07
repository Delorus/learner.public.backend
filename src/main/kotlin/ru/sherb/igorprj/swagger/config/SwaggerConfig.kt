package ru.sherb.igorprj.swagger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/**
 * @author maksim
 * @since 28.08.2019
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
            .build()
            .ignoredParameterTypes(Pageable::class.java)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()))
            .useDefaultResponseMessages(false)

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/v.*"))
                .build()
    }

    fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        return listOf(SecurityReference("JWT", arrayOf(authorizationScope)))
    }
}