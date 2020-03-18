package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.lineage.AtlasLineageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LineageRestService {

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    fun getLineage(guid: String): AtlasLineageInfo {
        return atlasFeignClient.getLineage(guid)
    }

}