package com.demo.atlasrest.web

import com.demo.atlasrest.service.RelationshipRestService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.extensions.Extension
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.instance.AtlasRelationship
import org.apache.atlas.model.lineage.AtlasLineageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController

@Tag(name="RelationshipREST", description = "REST interface for entity relationships.")
class RelationshipRestController :IBaseController{

    @Autowired
    lateinit var relationshipRestService: RelationshipRestService

    @GetMapping("/relationship/guid/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getRelationship(@PathVariable guid:String) : AtlasRelationship.AtlasRelationshipWithExtInfo {
        return relationshipRestService.getRelationship(guid)
    }

    @PostMapping("/relationship",consumes = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"],produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun createRelationShip(@RequestBody atlasRelationship:AtlasRelationship) : 	AtlasRelationship{
        return relationshipRestService.createRelationShip(atlasRelationship)
    }
}