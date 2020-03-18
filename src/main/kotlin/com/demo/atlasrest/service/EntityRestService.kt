package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.AtlasClientV2
import org.apache.atlas.model.instance.AtlasEntity
import org.apache.atlas.model.instance.AtlasEntity.AtlasEntitiesWithExtInfo
import org.apache.atlas.model.instance.AtlasEntityHeaders
import org.apache.atlas.model.instance.ClassificationAssociateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EntityRestService {

    /*@Autowired
    lateinit var atlasClientV2: AtlasClientV2*/

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun createEntity(): Any {
        return atlasFeignClient.createEntities(AtlasEntitiesWithExtInfo())
    }

    fun createClassification(classificationAssociateRequest: ClassificationAssociateRequest): Any {
        return atlasFeignClient.createClassification(classificationAssociateRequest)
    }

    fun setClassification(atlasEntityHeaders: AtlasEntityHeaders): Any {
        return atlasFeignClient.setClassification(atlasEntityHeaders)
    }

    fun getEntity(guid: String): AtlasEntity.AtlasEntityWithExtInfo {
        return atlasFeignClient.getEntity(guid)
    }

}