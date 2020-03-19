package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import com.demo.atlasrest.model.GlossaryModel
import org.apache.atlas.model.glossary.AtlasGlossary
import org.apache.atlas.model.glossary.AtlasGlossaryCategory
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.glossary.relations.AtlasGlossaryHeader
import org.apache.atlas.model.glossary.relations.AtlasTermCategorizationHeader
import org.apache.atlas.model.instance.AtlasRelatedObjectId
import org.apache.commons.lang3.StringUtils
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

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

    fun uploadGlossaries(file: MultipartFile): String {

        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val rows = sheet.iterator()

        if (rows.hasNext()) {
            for (i in 0..2) {
                rows.next()
            }
        }

        var list = mutableListOf<GlossaryModel>()

        while (rows.hasNext()) {
            val currentRow = rows.next()
            val cell = currentRow.getCell(1)
            if (cell != null && StringUtils.isNotBlank(cell.stringCellValue)) {

                val glossaryMgodel = GlossaryModel()
                glossaryMgodel.category = cell.stringCellValue.trim().replace(".", " ")
                glossaryMgodel.terms = currentRow.getCell(2).stringCellValue.trim().replace(".", " ")
                glossaryMgodel.descrition = currentRow.getCell(3).stringCellValue.trim()
                glossaryMgodel.long_descrition = currentRow.getCell(4).stringCellValue.trim()

                list.add(glossaryMgodel)
            } else {
                break
            }


        }
        workbook.close()

        val test: Map<String, List<GlossaryModel>> = list.groupBy { it.category }

        //create Glossary
        val rand = Random()
        val x = rand.nextInt(10000)
        val atlasGlossary = AtlasGlossary()
        atlasGlossary.qualifiedName = "SampleBank$x"
        atlasGlossary.name = "Banking$x"
        atlasGlossary.shortDescription = "Glossary of bank"
        atlasGlossary.longDescription = "Glossary of bank - long description"
        atlasGlossary.language = "English"
        atlasGlossary.usage = "N/A"

        val glossaryOutput = createGlossaries(atlasGlossary)
        val glossaryGuid = glossaryOutput.guid

        logger.info("Glossary Created>>>>> ${atlasGlossary.name}  >>>>> GUID >>>>> ${glossaryOutput.guid}")

        //create categories
        val atlasGlossaryCategoryList = mutableListOf<AtlasGlossaryCategory>()


        test.keys.forEach { key ->

            val atlasGlossaryCategory = AtlasGlossaryCategory()

            atlasGlossaryCategory.name = key

            val atlasGlossaryHeader = AtlasGlossaryHeader()

            atlasGlossaryHeader.glossaryGuid = glossaryGuid

            atlasGlossaryCategory.anchor = atlasGlossaryHeader

            atlasGlossaryCategoryList.add(
                    atlasGlossaryCategory
            )
        }

        val glossaryCategoryOutput = createGlossaryCategory(atlasGlossaryCategoryList)
        val glossaryCategoryOutputMap = glossaryCategoryOutput.groupBy { it.name }

        logger.info("category created: ")

        //glossaryTerms

        test.forEach { key, value ->
            val atlasGlossaryCategory = glossaryCategoryOutputMap.get(key)?.get(0)

            val atlasGlossaryTermList = mutableListOf<AtlasGlossaryTerm>()

            value.forEach { glossaryModel ->
                val atlasGlossaryTerm = AtlasGlossaryTerm()

                atlasGlossaryTerm.name = glossaryModel.terms
                atlasGlossaryTerm.shortDescription = glossaryModel.descrition
                atlasGlossaryTerm.longDescription = glossaryModel.long_descrition

                val atlasGlossaryHeader = AtlasGlossaryHeader()

                atlasGlossaryHeader.glossaryGuid = glossaryGuid

                atlasGlossaryTerm.anchor = atlasGlossaryHeader

                val cat = mutableSetOf<AtlasTermCategorizationHeader>()
                val header = AtlasTermCategorizationHeader()
                if (atlasGlossaryCategory != null) {
                    header.categoryGuid = atlasGlossaryCategory.guid
                }
                cat.add(header)
                atlasGlossaryTerm.categories = cat

                atlasGlossaryTermList.add(atlasGlossaryTerm)
            }

            createGlossaryTerms(atlasGlossaryTermList)
            logger.info("Terms added")
        }

        return "Glossary: " + atlasGlossary.name + " created successfully"
    }

}