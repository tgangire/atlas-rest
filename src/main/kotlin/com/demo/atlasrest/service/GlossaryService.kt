package com.demo.atlasrest.service

import com.demo.atlasrest.client.AtlasFeignClient
import com.demo.atlasrest.model.GlossaryModel
import org.apache.atlas.model.glossary.AtlasGlossary
import org.apache.atlas.model.glossary.AtlasGlossaryCategory
import org.apache.atlas.model.glossary.AtlasGlossaryTerm
import org.apache.atlas.model.glossary.relations.AtlasGlossaryHeader
import org.apache.atlas.model.glossary.relations.AtlasRelatedCategoryHeader
import org.apache.atlas.model.glossary.relations.AtlasRelatedTermHeader
import org.apache.atlas.model.glossary.relations.AtlasTermCategorizationHeader
import org.apache.atlas.model.instance.AtlasClassification
import org.apache.atlas.model.instance.AtlasRelatedObjectId
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.servlet.http.HttpServletResponse


@Service
class GlossaryService {

    private val logger = LoggerFactory.getLogger(GlossaryService::class.java)

    @Autowired
    lateinit var atlasFeignClient: AtlasFeignClient

    @Autowired
    lateinit var typesRestService: TypesRestService

    fun createGlossaries(atlasGlossary: AtlasGlossary): AtlasGlossary {
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

                if (Objects.isNull(currentRow.getCell(9))) {
                    glossaryMgodel.classification = ""
                } else {
                    glossaryMgodel.classification = currentRow.getCell(9).stringCellValue.trim()
                }

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

                //create Glossary Term
                val atlasGlossaryTerm = AtlasGlossaryTerm()

                atlasGlossaryTerm.name = glossaryModel.terms
                atlasGlossaryTerm.shortDescription = glossaryModel.descrition
                atlasGlossaryTerm.longDescription = glossaryModel.long_descrition

                val atlasGlossaryHeader = AtlasGlossaryHeader()

                atlasGlossaryHeader.glossaryGuid = glossaryGuid

                atlasGlossaryTerm.anchor = atlasGlossaryHeader

                var classifications = mutableListOf<AtlasClassification>()

                //add classification if value found
                if (StringUtils.isNotBlank(glossaryModel.classification)) {
                    val classification = AtlasClassification()

                    classification.typeName = glossaryModel.classification

                    classifications.add(classification)
                }

                atlasGlossaryTerm.classifications = classifications

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
            logger.info("Terms added for Glossary >>>>>> $key")
        }
        logger.info("Glossary $atlasGlossary.name created successfully")
        return "Glossary: " + atlasGlossary.name + " created successfully"
    }

    fun downloadGlossaries(response: HttpServletResponse) {

        response.contentType = "application/octet-stream"
        response.setHeader("Content-Disposition", "attachment; filename=Glossary.xlsx")

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Glossary")
        val termsBook = workbook.createSheet("Terms")
        val categoriesBook = workbook.createSheet("Category")
        val classificationBook = workbook.createSheet("Classification")

        val glossaries = getGlossaries()

        var termIndex = 0
        var categoryIndex = 0
        var classificationIndex = 0


        var x = ByteArrayOutputStream()
        glossaries.forEachIndexed { index, glossary ->

            var colNum = 0
            if (index == 0) {

                val row = sheet.createRow(index)
                createCell(row, "Name", 0)
                createCell(row, "QualifiedName", 1)
                createCell(row, "ShortDescription", 2)
                createCell(row, "LongDescription", 3)
                createCell(row, "Language", 4)
                createCell(row, "Guid", 5)

                val termRow = termsBook.createRow(termIndex++)
                createCell(termRow, "Glossary", 0)
                createCell(termRow, "Term", 1)
                createCell(termRow, "Classification", 2)

                val categoryRow = categoriesBook.createRow(categoryIndex++)
                createCell(categoryRow, "Glossary", 0)
                createCell(categoryRow, "Category", 1)

                val classificationRow = classificationBook.createRow(classificationIndex++)
                createCell(classificationRow, "Glossary", 0)
                createCell(classificationRow, "Classification", 1)
            }
            val row = sheet.createRow(index + 1)
            createCell(row, glossary.name, 0)
            createCell(row, glossary.qualifiedName, 1)

            var sd = ""
            sd = glossary.shortDescription?.toString().toString()
            createCell(row, sd, 3)

            var ld = ""
            ld = glossary.longDescription?.toString().toString()
            createCell(row, ld, 3)

            var lan = ""
            lan = glossary.language?.toString().toString()
            createCell(row, lan, 4)
            createCell(row, glossary.guid, 5)


            val terms: Set<AtlasRelatedTermHeader>? = glossary.terms
            if (terms != null && CollectionUtils.isNotEmpty(terms)) {
                terms.forEach { term ->

                    val termRow = termsBook.createRow(termIndex++)
                    createCell(termRow, glossary.name, 0)
                    createCell(termRow, term.displayText, 1)

                    val glossaryTerm: AtlasGlossaryTerm? = getGlossaryTerm(term.termGuid)
                    if (glossaryTerm != null) {

                        val classifications: List<AtlasClassification>? = glossaryTerm.classifications
                        if (classifications != null && CollectionUtils.isNotEmpty(classifications)) {

                            createCell(termRow, classifications.joinToString { atlasClassification -> atlasClassification.typeName }, 2)

                        }

                    }

                }

            }

            val categories: Set<AtlasRelatedCategoryHeader>? = glossary.categories
            if (categories != null && CollectionUtils.isNotEmpty(categories)) {
                categories.forEach { category ->
                    val categoryRow = categoriesBook.createRow(categoryIndex++)
                    createCell(categoryRow, glossary.name, 0)
                    createCell(categoryRow, category.displayText, 1)

                }
            }

            val classifications: List<AtlasClassification>? = glossary.classifications
            if (classifications != null && CollectionUtils.isNotEmpty(classifications)) {

                classifications.forEach { classfi ->
                    val classificationRow = classificationBook.createRow(classificationIndex++)
                    createCell(classificationRow, glossary.name, 0)
                    createCell(classificationRow, classfi.typeName, 1)
                }

            }


        }
        workbook.write(x)

        workbook.close()

        IOUtils.copy(ByteArrayInputStream(x.toByteArray()), response.outputStream)
        logger.info("File successfully downloaded")


    }

    private fun createCell(row: XSSFRow, value: String, number: Int) {
        var cell = row.createCell(number)
        cell.setCellValue(StringUtils.defaultIfBlank(value, ""))
    }

}
