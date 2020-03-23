package com.demo.atlasrest.web

import com.demo.atlasrest.service.EntityRestService
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.instance.AtlasEntity
import org.apache.atlas.model.instance.AtlasEntityHeaders
import org.apache.atlas.model.instance.ClassificationAssociateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name="EntityREST", description = "REST for a single entity.")
class EntityRestController : IBaseController{

    @Autowired
    lateinit var entityRestService: EntityRestService

    @PostMapping("/entity/bulk", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createEntities(@RequestBody atlasEntitiesWithExtInfo: AtlasEntity.AtlasEntitiesWithExtInfo): Any {
        return entityRestService.createEntities(atlasEntitiesWithExtInfo)
    }

    @PostMapping("/entity", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createEntity(@RequestBody atlasEntityWithExtInfo: AtlasEntity.AtlasEntityWithExtInfo): Any {
        return entityRestService.createEntity(atlasEntityWithExtInfo)
    }

    @PostMapping("/entity/bulk/classification", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createClassification(@RequestBody classificationAssociateRequest: ClassificationAssociateRequest): Any {
        return entityRestService.createClassification(classificationAssociateRequest)
    }

    @PostMapping("/entity/bulk/setClassification", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun setClassification(@RequestBody atlasEntityHeaders: AtlasEntityHeaders): Any {
        return entityRestService.setClassification(atlasEntityHeaders)
    }

    @GetMapping("/entity/guid/{guid}", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getEntity(@PathVariable guid: String): AtlasEntity.AtlasEntityWithExtInfo {
        return entityRestService.getEntity(guid)
    }

    @GetMapping("/entities", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getEntities(): AtlasEntity.AtlasEntitiesWithExtInfo {
        return entityRestService.getEntities()
    }

    @PostMapping("/entity/upload")
    fun uploadEntity(@RequestParam("file") file: MultipartFile, @RequestParam tableName: String, @RequestParam tableTypeDef: String, @RequestParam columnTypeDef: String): String {
        return entityRestService.uploadEntity(file, tableName, tableTypeDef, columnTypeDef)
    }

    @PostMapping("/entity/addendum/upload")
    fun uploadAddendumEntity(@RequestParam("file") file: MultipartFile, @RequestParam tableName: String, @RequestParam tableTypeDef: String, @RequestParam columnTypeDef: String): String {
        return entityRestService.uploadAddendumEntity(file, tableName, tableTypeDef, columnTypeDef)
    }

}