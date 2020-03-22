package com.demo.atlasrest.web

import com.demo.atlasrest.service.TypesRestService
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.typedef.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name="TypesREST", description = "REST interface for CRUD operations on type definitions.")
class TypesRestController :IBaseController{

    @Autowired
    lateinit var typesRestService: TypesRestService

    @GetMapping("/types/classificationdef/guid/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesClassificationDefByGuid(@PathVariable guid:String) : AtlasClassificationDef {
        return typesRestService.getTypesClassificationDefByGuid(guid)
    }

    @GetMapping("/types/classificationdef/name/{name}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesClassificationDefByName(@PathVariable name:String) : AtlasClassificationDef {
        return typesRestService.getTypesClassificationDefByName(name)
    }

    @GetMapping("/types/entitydef/guid/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesEntityDefByGuid(@PathVariable guid:String) : AtlasEntityDef {
        return typesRestService.getTypesEntityDefByGuid(guid)
    }

    @GetMapping("/types/entitydef/name/{name}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesEntityDefByName(@PathVariable name:String) : AtlasEntityDef {
        return typesRestService.getTypesEntityDefByName(name)
    }

    @GetMapping("/types/relationshipdef/guid/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun  getTypesRelationshipDefByGuid(@PathVariable guid:String) : AtlasRelationshipDef {
        return typesRestService.getTypesRelationshipDefByGuid(guid)
    }

    @GetMapping("/types/relationshipdef/name/{name}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesRelationshipDefByName(@PathVariable name:String) : AtlasRelationshipDef {
        return typesRestService.getTypesRelationshipDefByName(name)
    }

    @GetMapping("/types/typedef/guid/{guid}",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun  getTypesTypeDefByGuid(@PathVariable guid:String) : AtlasBaseTypeDef {
        return typesRestService.getTypesTypeDefByGuid(guid)
    }

    @GetMapping("/types/typedef/name/{name}", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getTypesTypeDefByName(@PathVariable name: String): Any {
        return typesRestService.getTypesTypeDefByName(name)
    }

    @GetMapping("/types/typedefs",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesTypeDefs() : AtlasTypesDef {
        return typesRestService.getTypesTypeDefs()
    }

    @GetMapping("/types/typedefs/headers",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getTypesTypeDefsHeaders() : List<AtlasTypeDefHeader> {
        return typesRestService.getTypesTypeDefsHeaders()
    }

    @PostMapping("/types/typedefs",consumes = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"],produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun createTypesTypeDefs(@RequestBody atlasTypesDef: AtlasTypesDef) : AtlasTypesDef {
        return typesRestService.createTypesTypeDefs(atlasTypesDef)
    }

    @PutMapping("/types/typedefs",consumes = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"],produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun updateTypesTypeDefs(@RequestBody atlasTypesDef: AtlasTypesDef) : AtlasTypesDef {
        return typesRestService.updateTypesTypeDefs(atlasTypesDef)
    }

}