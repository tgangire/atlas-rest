package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.instance.AtlasEntity
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

    fun createEntity(atlasEntityWithExtInfo: AtlasEntity.AtlasEntityWithExtInfo): Any {
        return atlasFeignClient.createEntity(atlasEntityWithExtInfo)
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

    fun createEntities(atlasEntityWithExtInfo: AtlasEntity.AtlasEntityWithExtInfo): Any {
        return atlasFeignClient.createEntities(atlasEntityWithExtInfo)
    }

}