package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.AtlasClientV2
import org.apache.atlas.model.glossary.AtlasGlossary
import org.apache.atlas.model.glossary.AtlasGlossaryCategory
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.instance.AtlasRelatedObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GlossaryService {

    private val logger = LoggerFactory.getLogger(GlossaryService::class.java)

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun createGlossaries(atlasGlossary: AtlasGlossary) :AtlasGlossary{
        logger.info("In Get Glossaries")

        return atlasFeignClient.createGlossaries(atlasGlossary)
    }

    fun createGlossaryCategory(atlasGlossaryCategoryList: List<AtlasGlossaryCategory>): List<AtlasGlossaryCategory> {
        return atlasFeignClient.createGlossaryCategory(atlasGlossaryCategoryList)
    }

    fun createGlossaryTerms(atlasGlossaryTermList: List<AtlasGlossaryTerm>): List<AtlasGlossaryTerm> {
        return atlasFeignClient.createGlossaryTerms(atlasGlossaryTermList)
    }

    fun createGlossaryTermAssignedEntities(termGuid: String, atlasRelatedObjectIdList: List<AtlasRelatedObjectId>): Any {
        return atlasFeignClient.createGlossaryTermAssignedEntities(termGuid,atlasRelatedObjectIdList)
    }

    fun getGlossaries(): List<AtlasGlossary> {
        return atlasFeignClient.getGlossaries()
    }

    fun getGlossaryCategory(categoryGuid: String): AtlasGlossaryCategory {
        return atlasFeignClient.getGlossaryCategory(categoryGuid)
    }

    fun getGlossaryTerm(termGuid: String): AtlasGlossaryTerm {
        return atlasFeignClient.getGlossaryTerm(termGuid)
    }

    fun getGlossaryTermAssignedEntities(termGuid: String): List<AtlasRelatedObjectId> {
        return atlasFeignClient.getGlossaryTermAssignedEntities(termGuid)
    }

}