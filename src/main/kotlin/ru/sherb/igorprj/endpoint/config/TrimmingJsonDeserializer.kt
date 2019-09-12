package ru.sherb.igorprj.endpoint.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.boot.jackson.JsonComponent
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder


/**
 * @author maksim
 * @since 12.09.2019
 */
@JsonComponent
class TrimmingJsonDeserializer : JsonDeserializer<String>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext)
            = if (p.hasToken(JsonToken.VALUE_STRING)) p.text?.trim() else null
}

@ControllerAdvice
class ControllerSetup {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
    }
}