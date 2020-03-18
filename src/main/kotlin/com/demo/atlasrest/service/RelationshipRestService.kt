package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.instance.AtlasRelationship
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RelationshipRestService {

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun getRelationship(guid: String): AtlasRelationship.AtlasRelationshipWithExtInfo {
        return atlasFeignClient.getRelationship(guid)
    }

    fun createRelationShip(atlasRelationship: AtlasRelationship): AtlasRelationship {
        return atlasFeignClient.createRelationShip(atlasRelationship)
    }

}