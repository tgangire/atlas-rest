package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.typedef.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TypesRestService {

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun getTypesClassificationDefByGuid(guid: String): AtlasClassificationDef {
        return atlasFeignClient.getTypesClassificationDefByGuid(guid)
    }

    fun getTypesClassificationDefByName(name: String): AtlasClassificationDef {
        return atlasFeignClient.getTypesClassificationDefByName(name)
    }

    fun getTypesEntityDefByGuid(guid: String): AtlasEntityDef {
        return atlasFeignClient.getTypesEntityDefByGuid(guid)
    }

    fun getTypesEntityDefByName(name: String): AtlasEntityDef {
        return atlasFeignClient.getTypesEntityDefByName(name)
    }

    fun getTypesRelationshipDefByGuid(guid: String): AtlasRelationshipDef {
        return atlasFeignClient.getTypesRelationshipDefByGuid(guid)
    }

    fun getTypesRelationshipDefByName(name: String): AtlasRelationshipDef {
        return atlasFeignClient.getTypesRelationshipDefByName(name)
    }

    fun getTypesTypeDefByGuid(guid: String): AtlasBaseTypeDef {
        return atlasFeignClient.getTypesTypeDefByGuid(guid)
    }

    fun getTypesTypeDefByName(name: String): AtlasBaseTypeDef {
        return atlasFeignClient.getTypesTypeDefByName(name)
    }

    fun getTypesTypeDefs(): AtlasTypesDef {
        return atlasFeignClient.getTypesTypeDefs()
    }

    fun getTypesTypeDefsHeaders(): List<AtlasTypeDefHeader> {
        return atlasFeignClient.getTypesTypeDefsHeaders()
    }

    fun createTypesTypeDefs(atlasTypesDef: AtlasTypesDef): AtlasTypesDef {
        return atlasFeignClient.createTypesTypeDefs(atlasTypesDef)
    }

    fun updateTypesTypeDefs(atlasTypesDef: AtlasTypesDef): AtlasTypesDef {
        return atlasFeignClient.updateTypesTypeDefs(atlasTypesDef)
    }


}