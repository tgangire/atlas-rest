package com.demo.atlasrest.web

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.awt.PageAttributes

@RestController

class TestController {

    @GetMapping("/test/v2/glossary",produces = [MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"])
    fun test() : String{
        return "Hello World"
    }

}