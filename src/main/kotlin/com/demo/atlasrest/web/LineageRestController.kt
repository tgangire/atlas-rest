package com.demo.atlasrest.web

import com.demo.atlasrest.service.LineageRestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.lineage.AtlasLineageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name="LineageREST", description = "REST interface for an entity's lineage information.")
class LineageRestController :IBaseController{

    @Autowired
    lateinit var lineageRestService: LineageRestService

    @Operation(summary = "Get Lineage By Guid", description = "Get Lineage By Guid")
    @GetMapping("/lineage/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getLineage(@PathVariable guid:String) : AtlasLineageInfo {
        return lineageRestService.getLineage(guid)
    }

}