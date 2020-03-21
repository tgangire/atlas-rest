package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.discovery.AtlasSearchResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiscoveryRestService {

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun getAttribute(attributeValue: String, typeName: String): AtlasSearchResult {
        return atlasFeignClient.getAttribute(attributeValue, typeName, 10, 0)
    }

    fun getAttributesSearchBasic(query: String, typeName: String): AtlasSearchResult {
        return atlasFeignClient.getAttributesSearchBasic(query, typeName)
    }
}