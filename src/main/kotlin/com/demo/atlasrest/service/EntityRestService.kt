package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import org.apache.atlas.model.instance.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


@Service
class EntityRestService {

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    @Autowired
    lateinit var glossaryService: GlossaryService

    fun createEntity(atlasEntityWithExtInfo: AtlasEntity.AtlasEntityWithExtInfo): EntityMutationResponse {
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

    fun uploadEntity(file: MultipartFile, tableName: String, tableTypeDef: String, columnTypeDef: String): String {

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
        //atlasEntity.typeName = "hive_table"
        atlasEntity.typeName = tableTypeDef
        val attributes = mutableMapOf<String, Any>()
        attributes.put("owner", "hive")
        attributes.put("temporary", "false")
        //val tableName = "test_table" + rand.nextInt(1000)
        //  val tableName = "D002"
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
        val ra = createMap(tableTypeDef).toMutableMap()
        ra["guid"] = atlasEntity.guid
        referencedAttributes.put("table", ra)
        referencedAttributes.put("numBuckets", -1)
        referencedAtlasEntity.attributes = referencedAttributes
        referredEntities.put(sd.get("guid").toString(), referencedAtlasEntity)


        var columns = ArrayList<Map<String, String>>()
        var assignedEntities = mutableMapOf<String, String>()
        var glossaries = glossaryService.getGlossaries()
        val glossaryTermsByName = glossaries.associateBy({ it.name }, { it.terms })

        while (rows.hasNext()) {

            val currentRow = rows.next()

            val columnMap = createMap(columnTypeDef).toMutableMap()
            columns.add(columnMap)


            val referencedAtlasEntity1 = AtlasEntity()

            referencedAtlasEntity1.guid = columnMap.get("guid")
            referencedAtlasEntity1.status = AtlasEntity.Status.ACTIVE
            referencedAtlasEntity1.version = 0
            referencedAtlasEntity1.typeName = columnTypeDef

            val referencedAttributes1 = mutableMapOf<String, Any>()
            referencedAttributes1.put("owner", "hive")
            referencedAttributes1.put("name", currentRow.getCell(0).stringCellValue)
            referencedAttributes1.put("comment", currentRow.getCell(2).stringCellValue)
            referencedAttributes1.put("description", currentRow.getCell(1).stringCellValue)
            referencedAttributes1.put("qualifiedName", "default." + tableName + "." + currentRow.getCell(0).stringCellValue + "@hdp_cluster")
            referencedAttributes1.put("type", currentRow.getCell(3).stringCellValue)
            referencedAttributes1.put("inputFieldName", getCellValueAsString(currentRow, 8))
            referencedAttributes1.put("outputFieldName", getCellValueAsString(currentRow, 9))
            referencedAttributes1.put("format", getCellValueAsString(currentRow, 10))

            if (!Objects.isNull(currentRow.getCell(11))) {
                referencedAttributes1.put("length", currentRow.getCell(11).numericCellValue.toInt())
            }
            referencedAttributes1.put("sourceType", getCellValueAsString(currentRow, 12))
            referencedAttributes1.put("dataType", getCellValueAsString(currentRow, 13))

            if (!Objects.isNull(currentRow.getCell(14))) {
                referencedAttributes1.put("lengthPrecision", currentRow.getCell(14).numericCellValue.toInt())
            }
            referencedAttributes1.put("scale", getCellValueAsString(currentRow, 15))
            referencedAttributes1.put("defaultValue", getCellValueAsString(currentRow, 16))
            referencedAttributes1.put("possibleValues", getCellValueAsString(currentRow, 17).split(","))
            referencedAttributes1.put("isPrimaryKey", getCellValueAsString(currentRow, 18))
            referencedAttributes1.put("isNullable", getCellValueAsString(currentRow, 19))
            referencedAttributes1.put("isForeignKey", getCellValueAsString(currentRow, 20))
            referencedAttributes1.put("foreignKeyCondition", getCellValueAsString(currentRow, 21))


            val ra1 = createMap(tableTypeDef).toMutableMap()
            ra1["guid"] = atlasEntity.guid
            referencedAttributes1.put("table", ra1)
            referencedAtlasEntity1.attributes = referencedAttributes1

            //assign term to columns


            if (!Objects.isNull(currentRow.getCell(7))) {
                var term = currentRow.getCell(7).stringCellValue.trim().replace(".", " ")
                var glossaryName = currentRow.getCell(6).stringCellValue.trim().replace(".", " ")

                var termGuid = glossaryTermsByName[glossaryName]?.stream()?.filter { terms -> terms.displayText.equals(term) }?.findFirst()

                if (termGuid != null) {
                    if (termGuid.isPresent) {
                        assignedEntities.put(referencedAtlasEntity1.guid, termGuid.get().termGuid)
                    }
                }
            }
            referredEntities.put(columnMap.get("guid").toString(), referencedAtlasEntity1)


        }

        attributes["columns"] = columns

        workbook.close()
        atlasEntity.attributes = attributes
        atlasEntityWithExtInfo.referredEntities = referredEntities
        atlasEntityWithExtInfo.entity = atlasEntity

        val entityMutationResponse = createEntity(atlasEntityWithExtInfo)

        val guidAssignments = entityMutationResponse.guidAssignments

        assignedEntities.forEach { (key, value) ->

            val atlasRelatedObjectIds = mutableListOf<AtlasRelatedObjectId>()
            val atlasRelatedObjectId = AtlasRelatedObjectId()
            atlasRelatedObjectId.guid = guidAssignments.get(key)
            atlasRelatedObjectIds.add(atlasRelatedObjectId)

            glossaryService.createGlossaryTermAssignedEntities(value, atlasRelatedObjectIds)
        }
        println("table $tableName created")

        return "$tableName Created Successfully"
    }

    fun getCellValueAsString(currentRow: Row, num: Int): String {
        if (!Objects.isNull(currentRow.getCell(num))) {
            return currentRow.getCell(num).stringCellValue
        }
        return ""
    }

    fun createMap(typeName: String): Map<String, String> {
        val map = mutableMapOf<String, String>()

        map["guid"] = "-" + Random.nextInt().toString()
        map["typeName"] = typeName

        return map

    }

    fun getEntities(): AtlasEntity.AtlasEntitiesWithExtInfo {
        return atlasFeignClient.getEntities()
    }

    fun uploadAddendumEntity(file: MultipartFile, tableName: String, tableTypeDef: String, columnTypeDef: String): String {

        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val rows = sheet.iterator()

        //skip headers
        rows.next()

        val atlasEntityWithExtInfo = AtlasEntity.AtlasEntityWithExtInfo()
        val rand = java.util.Random()
        val atlasEntity = AtlasEntity()
        atlasEntity.guid = "-" + Random.nextInt(15).toString()
        atlasEntity.status = AtlasEntity.Status.ACTIVE
        atlasEntity.version = 0
        atlasEntity.typeName = tableTypeDef
        val attributes = mutableMapOf<String, Any>()
        attributes.put("owner", "hive")
        attributes.put("temporary", "false")
        // val tableName = "test_table" + rand.nextInt(1000)
        // val tableName = "D002"
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

        val ra = createMap(tableTypeDef).toMutableMap()
        ra["guid"] = atlasEntity.guid
        referencedAttributes.put("table", ra)
        referencedAttributes.put("numBuckets", -1)
        referencedAtlasEntity.attributes = referencedAttributes
        referredEntities.put(sd.get("guid").toString(), referencedAtlasEntity)

        val columns = ArrayList<Map<String, String>>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        while (rows.hasNext()) {

            val currentRow = rows.next()

            val columnMap = createMap(columnTypeDef).toMutableMap()
            columns.add(columnMap)


            val referencedAtlasEntity1 = AtlasEntity()

            referencedAtlasEntity1.guid = columnMap.get("guid")
            referencedAtlasEntity1.status = AtlasEntity.Status.ACTIVE
            referencedAtlasEntity1.version = 0
            referencedAtlasEntity1.typeName = columnTypeDef

            val referencedAttributes1 = mutableMapOf<String, Any>()
            referencedAttributes1.put("owner", "hive")
            referencedAttributes1.put("name", currentRow.getCell(3).stringCellValue.trim())
            /*referencedAttributes1.put("comment", currentRow.getCell(2).stringCellValue)
            referencedAttributes1.put("description", currentRow.getCell(1).stringCellValue)
*/            referencedAttributes1.put("qualifiedName", "default." + tableName + "." + currentRow.getCell(3).stringCellValue.trim() + "@hdp_cluster")
            referencedAttributes1.put("type", "VARCHAR")
            referencedAttributes1.put("BUSINESS_DATE", currentRow.getCell(0).dateCellValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter))
            referencedAttributes1.put("DQ_REF_RULE_ID", currentRow.getCell(1).numericCellValue.toInt())
            referencedAttributes1.put("DQ_RULE_NAME", currentRow.getCell(2).stringCellValue)
            referencedAttributes1.put("SEVERITY", currentRow.getCell(4).stringCellValue)
            referencedAttributes1.put("Pass", currentRow.getCell(5).numericCellValue)
            referencedAttributes1.put("Fail", currentRow.getCell(6).numericCellValue)
            referencedAttributes1.put("Exclude", currentRow.getCell(7).numericCellValue)
            referencedAttributes1.put("TOTAL", currentRow.getCell(8).numericCellValue)
            referencedAttributes1.put("Pass %", currentRow.getCell(9).numericCellValue.toString())

            val ra1 = createMap(tableTypeDef).toMutableMap()
            ra1["guid"] = atlasEntity.guid
            referencedAttributes1.put("table", ra1)
            referencedAtlasEntity1.attributes = referencedAttributes1

            referredEntities.put(columnMap.get("guid").toString(), referencedAtlasEntity1)

        }

        attributes["columns"] = columns

        workbook.close()
        atlasEntity.attributes = attributes
        atlasEntityWithExtInfo.referredEntities = referredEntities
        atlasEntityWithExtInfo.entity = atlasEntity

        createEntity(atlasEntityWithExtInfo)

        return "Entity  $tableName Created"
    }
}


