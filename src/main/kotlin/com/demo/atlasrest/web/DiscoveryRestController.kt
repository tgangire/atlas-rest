package com.demo.atlasrest.web

import com.demo.atlasrest.service.DiscoveryRestService
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.discovery.AtlasSearchResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "DiscoveryREST", description = "REST interface for data discovery using dsl or full text search.")
class DiscoveryRestController : IBaseController {

    @Autowired
    lateinit var discoveryRestService: DiscoveryRestService

    @GetMapping("/search/attribute", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getAttribute(@RequestParam attributeValue: String, @RequestParam typeName: String): AtlasSearchResult {
        return discoveryRestService.getAttribute(attributeValue, typeName)
    }

    @GetMapping("/search/basic", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getAttributesSearchBasic(@RequestParam attributeValue: String, @RequestParam typeName: String): AtlasSearchResult {
        return discoveryRestService.getAttributesSearchBasic(attributeValue, typeName)
    }


}