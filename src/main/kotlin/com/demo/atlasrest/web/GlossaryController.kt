package com.demo.atlasrest.web

import com.demo.atlasrest.service.GlossaryService
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.atlas.model.glossary.AtlasGlossary
import org.apache.atlas.model.glossary.AtlasGlossaryCategory
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.instance.AtlasRelatedObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@Tag(name="GlossaryREST", description = "REST for Glossary.")
class GlossaryController :IBaseController {

    @Autowired
    lateinit var glossaryService: GlossaryService

    @PostMapping("/glossary", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createGlossaries(@RequestBody atlasGlossary: AtlasGlossary): AtlasGlossary {
        return glossaryService.createGlossaries(atlasGlossary)
    }

    @PostMapping("/glossary/upload")
    fun uploadGlossaries(@RequestParam("file") file: MultipartFile): String {
        return glossaryService.uploadGlossaries(file)
    }

    @PostMapping("/glossary/categories", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createGlossaryCategory(@RequestBody atlasGlossaryCategoryList: List<AtlasGlossaryCategory>): List<AtlasGlossaryCategory> {
        return glossaryService.createGlossaryCategory(atlasGlossaryCategoryList)
    }

    @PostMapping("/glossary/terms", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun createGlossaryTerms(@RequestBody atlasGlossaryTermList: List<AtlasGlossaryTerm>): List<AtlasGlossaryTerm> {
        return glossaryService.createGlossaryTerms(atlasGlossaryTermList)
    }

    @PostMapping("/glossary/terms/{termGuid}/assignedEntities", consumes = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"], produces = [MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"])
    fun createGlossaryTermAssignedEntities(@PathVariable termGuid: String, @RequestBody atlasRelatedObjectIdList: List<AtlasRelatedObjectId>): Any {
        return glossaryService.createGlossaryTermAssignedEntities(termGuid, atlasRelatedObjectIdList)
    }

    @GetMapping("/glossary", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getGlossaries(): List<AtlasGlossary> {
        return glossaryService.getGlossaries()
    }

    @GetMapping("/download/glossary", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8"])
    @ResponseBody
    fun downloadGlossaries(response: HttpServletResponse) {
        glossaryService.downloadGlossaries(response)
    }

    @GetMapping("/glossary/category/{categoryGuid}", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getGlossaryCategory(@PathVariable categoryGuid: String): AtlasGlossaryCategory {
        return glossaryService.getGlossaryCategory(categoryGuid)
    }

    @GetMapping("/glossary/term/{termGuid}", produces = [MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"])
    fun getGlossaryTerm(@PathVariable termGuid: String): AtlasGlossaryTerm {
        return glossaryService.getGlossaryTerm(termGuid)
    }

    @GetMapping("/glossary/terms/{termGuid}/assignedEntities",produces = [MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"])
    fun getGlossaryTermAssignedEntities(@PathVariable termGuid:String) : List<AtlasRelatedObjectId>{
        return glossaryService.getGlossaryTermAssignedEntities(termGuid)
    }

}