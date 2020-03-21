package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.instance.AtlasEntity
import org.apache.atlas.model.instance.AtlasEntityHeaders
import org.apache.atlas.model.instance.ClassificationAssociateRequest
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.random.Random

@Service
class EntityRestService {

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

    fun createEntities(atlasEntityWithExtInfo: AtlasEntity.AtlasEntitiesWithExtInfo): Any {
        return atlasFeignClient.createEntities(atlasEntityWithExtInfo)
    }

    fun uploadEntity(file: MultipartFile): String {

        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(1)
        val rows = sheet.iterator()

        if (rows.hasNext()) {
            for (i in 0..2) {
                rows.next()
            }
        }

        val atlasEntityWithExtInfo = AtlasEntity.AtlasEntityWithExtInfo()
        val rand = java.util.Random()
        val atlasEntity = AtlasEntity()
        atlasEntity.guid = "-" + Random.nextInt(15).toString()
        atlasEntity.status = AtlasEntity.Status.ACTIVE
        atlasEntity.version = 0
        atlasEntity.typeName = "hive_table"
        val attributes = mutableMapOf<String, Any>()
        attributes.put("owner", "hive")
        attributes.put("temporary", "false")
        val tableName = "test_table" + rand.nextInt(1000)
        attributes.put("name", tableName)
        val qualifiedName = "default." + tableName + "@hdp_cluster"
        attributes.put("qualifiedName", qualifiedName)
        attributes.put("tableType", "MANAGED_TABLE")


        val sd = createMap("hive_storagedesc")

        attributes.put("sd", sd)


        val referredEntities = mutableMapOf<String, AtlasEntity>()
        val referencedAtlasEntity = AtlasEntity()
        referencedAtlasEntity.guid = sd.get("guid")
        referencedAtlasEntity.status = AtlasEntity.Status.ACTIVE
        referencedAtlasEntity.version = 0
        referencedAtlasEntity.typeName = "hive_storagedesc"

        val referencedAttributes = mutableMapOf<String, Any>()
        referencedAttributes.put("storedAsSubDirectories", false)
        referencedAttributes.put("location", "hdfs://ip-172-31-19-246.ap-southeast-1.compute.internal:8020/warehouse/tablespace/managed/hive")
        referencedAttributes.put("compressed", false)
        referencedAttributes.put("qualifiedName", qualifiedName)
        referencedAttributes.put("inputFormat", "org.apache.hadoop.mapred.TextInputFormat")
        referencedAttributes.put("outputFormat", "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat")
        referencedAttributes.put("parameters", {})
        /*  referencedAttributes.put("serdeInfo","{\n" +
                  "          \"typeName\": \"hive_serde\",\n" +
                  "          \"attributes\": {\n" +
                  "            \"serializationLib\": \"org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe\",\n" +
                  "            \"parameters\": {\n" +
                  "              \"serialization.format\": \"1\"\n" +
                  "            }\n" +
                  "          }\n" +
                  "        }")
  */
        val ra = createMap("hive_table").toMutableMap()
        ra["guid"] = atlasEntity.guid
        referencedAttributes.put("table", ra)
        referencedAttributes.put("numBuckets", -1)
        referencedAtlasEntity.attributes = referencedAttributes
        referredEntities.put(sd.get("guid").toString(), referencedAtlasEntity)


        var columns = ArrayList<Map<String, String>>()
        while (rows.hasNext()) {

            val currentRow = rows.next()

            val columnMap = createMap("hive_column").toMutableMap()
            columns.add(columnMap)


            val referencedAtlasEntity1 = AtlasEntity()

            referencedAtlasEntity1.guid = columnMap.get("guid")
            referencedAtlasEntity1.status = AtlasEntity.Status.ACTIVE
            referencedAtlasEntity1.version = 0
            referencedAtlasEntity1.typeName = "hive_column"

            val referencedAttributes1 = mutableMapOf<String, Any>()
            referencedAttributes1.put("owner", "hive")
            referencedAttributes1.put("name", currentRow.getCell(0).stringCellValue)
            referencedAttributes1.put("comment", currentRow.getCell(2).stringCellValue)
            referencedAttributes1.put("description", currentRow.getCell(1).stringCellValue)
            referencedAttributes1.put("qualifiedName", "default." + tableName + "." + currentRow.getCell(0).stringCellValue + "@hdp_cluster")
            referencedAttributes1.put("type", currentRow.getCell(3).stringCellValue)
            val ra1 = createMap("hive_table").toMutableMap()
            ra1["guid"] = atlasEntity.guid
            referencedAttributes1.put("table", ra1)
            referencedAtlasEntity1.attributes = referencedAttributes1

            referredEntities.put(columnMap.get("guid").toString(), referencedAtlasEntity1)


        }

        attributes.put("columns", columns)

        workbook.close()
        atlasEntity.attributes = attributes
        atlasEntityWithExtInfo.referredEntities = referredEntities
        atlasEntityWithExtInfo.entity = atlasEntity
        createEntity(atlasEntityWithExtInfo)

        println("table $tableName created")

        return tableName + " Created Successfully"
    }

    fun createMap(typeName: String): Map<String, String> {
        val map = mutableMapOf<String, String>()

        map["guid"] = "-" + Random.nextInt().toString()
        map["typeName"] = typeName

        return map

    }
}


