package com.demo.atlasrest.client

import com.demo.atlasrest.configuration.FeignClientConfiguration
import org.apache.atlas.model.discovery.AtlasSearchResult
import org.apache.atlas.model.glossary.AtlasGlossary
import org.apache.atlas.model.glossary.AtlasGlossaryCategory
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.instance.*
import org.apache.atlas.model.lineage.AtlasLineageInfo
import org.apache.atlas.model.typedef.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*

@Configuration
@FeignClient(value = "atlas-rest", url = "\${atlas.rest.url}", configuration = [FeignClientConfiguration::class])
interface AtlasFeignClient {

    /** Entity Rest Started **/

    @PostMapping("/v2/entity")
    fun createEntity(atlasEntityWithExtInfo: AtlasEntity.AtlasEntityWithExtInfo): EntityMutationResponse

    @PostMapping("/v2/entity/bulk")
    fun createEntities(@RequestBody atlasEntitiesWithExtInfo: AtlasEntity.AtlasEntitiesWithExtInfo): Any

    @PostMapping("/v2/entity/bulk/classification")
    fun createClassification(@RequestBody classificationAssociateRequest: ClassificationAssociateRequest): Any

    @PostMapping("/v2/entity/bulk/setClassifications")
    fun setClassification(@RequestBody atlasEntityHeaders: AtlasEntityHeaders): Any

    @GetMapping("/v2/entity/guid/{guid}")
    fun getEntity(@PathVariable guid: String): AtlasEntity.AtlasEntityWithExtInfo

    @GetMapping("/v2/entity/bulk")
    fun getEntities(): AtlasEntity.AtlasEntitiesWithExtInfo

    /** Entity Rest End **/

    /** Glossary Rest Started **/
    @PostMapping("/v2/glossary")
    fun createGlossaries(@RequestBody atlasGlossary: AtlasGlossary): AtlasGlossary

    @PostMapping("/v2/glossary/categories")
    fun createGlossaryCategory(@RequestBody atlasGlossaryCategoryList: List<AtlasGlossaryCategory>): List<AtlasGlossaryCategory>

    @PostMapping("/v2/glossary/terms")
    fun createGlossaryTerms(@RequestBody atlasGlossaryTermList: List<AtlasGlossaryTerm>): List<AtlasGlossaryTerm>

    @PostMapping("/v2/glossary/terms/{termGuid}/assignedEntities")
    fun createGlossaryTermAssignedEntities(@PathVariable termGuid: String, @RequestBody atlasRelatedObjectIdList: List<AtlasRelatedObjectId>): Any

    @GetMapping("/v2/glossary")
    fun getGlossaries(): List<AtlasGlossary>

    @GetMapping("/v2/glossary/category/{categoryGuid}")
    fun getGlossaryCategory(@PathVariable categoryGuid: String): AtlasGlossaryCategory

    @GetMapping("/v2/glossary/term/{termGuid}")
    fun getGlossaryTerm(@PathVariable termGuid: String): AtlasGlossaryTerm

    @GetMapping("/v2/glossary/terms/{termGuid}/assignedEntities")
    fun getGlossaryTermAssignedEntities(@PathVariable termGuid: String): List<AtlasRelatedObjectId>

    /** Glossary Rest End **/

    /** Lineage Rest Start **/
    @GetMapping("/v2/lineage/{guid}")
    fun getLineage(@PathVariable guid: String): AtlasLineageInfo
    /** Lineage Rest End **/

    /** Relationship Rest Start **/
    @GetMapping("/v2/relationship/guid/{guid}")
    fun getRelationship(@PathVariable guid: String): AtlasRelationship.AtlasRelationshipWithExtInfo

    @GetMapping("/v2/relationship")
    fun createRelationShip(@RequestBody atlasRelationship: AtlasRelationship): AtlasRelationship

    /** Relationship Rest End **/

    /** TypesRest Rest Start **/

    @GetMapping("/v2/types/classificationdef/guid/{guid}")
    fun getTypesClassificationDefByGuid(@PathVariable guid: String): AtlasClassificationDef

    @GetMapping("/v2/types/classificationdef/name/{name}")
    fun getTypesClassificationDefByName(@PathVariable name: String): AtlasClassificationDef

    @GetMapping("/v2/types/entitydef/guid/{guid}")
    fun getTypesEntityDefByGuid(@PathVariable guid: String): AtlasEntityDef

    @GetMapping("/v2/types/entitydef/name/{name}")
    fun getTypesEntityDefByName(@PathVariable name: String): AtlasEntityDef

    @GetMapping("/v2/types/relationshipdef/guid/{guid}")
    fun getTypesRelationshipDefByGuid(@PathVariable guid: String): AtlasRelationshipDef

    @GetMapping("/v2/types/relationshipdef/name/{name}")
    fun getTypesRelationshipDefByName(@PathVariable name: String): AtlasRelationshipDef

    @GetMapping("/v2/types/typedef/guid/{guid}")
    fun getTypesTypeDefByGuid(@PathVariable guid: String): AtlasBaseTypeDef

    @GetMapping("/v2/types/typedef/name/{name}")
    fun getTypesTypeDefByName(@PathVariable name: String): Any

    @GetMapping("/v2/types/typedefs")
    fun getTypesTypeDefs(): AtlasTypesDef

    @GetMapping("/v2/types/typedefs/headers")
    fun getTypesTypeDefsHeaders(): List<AtlasTypeDefHeader>

    @PostMapping("/v2/types/typedefs")
    fun createTypesTypeDefs(@RequestBody atlasTypesDef: AtlasTypesDef): AtlasTypesDef

    @PutMapping("/v2/types/typedefs")
    fun updateTypesTypeDefs(@RequestBody atlasTypesDef: AtlasTypesDef): AtlasTypesDef

    /** TypesRest Rest End **/

    /** Discovery Rest Start **/
    @GetMapping("/v2/search/attribute")
    fun getAttribute(@RequestParam attrValuePrefix: String, @RequestParam typeName: String, @RequestParam(defaultValue = "10") limit: Int, @RequestParam(defaultValue = "0") offset: Int): AtlasSearchResult

    @GetMapping("/v2/search/basic")
    fun getAttributesSearchBasic(@RequestParam query: String, @RequestParam typeName: String): AtlasSearchResult
    /** Discovery Rest End **/
}