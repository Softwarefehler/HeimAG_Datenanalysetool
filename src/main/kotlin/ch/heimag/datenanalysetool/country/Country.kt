package ch.heimag.datenanalysetool.country

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import org.slf4j.LoggerFactory


class Country {
  private  val logger = LoggerFactory.getLogger("FileReader.Country")

    fun loadToSelectCountry(resourcePath: String): MutableList<String> {
         logger.info("Load Countries from File: $resourcePath")
        val countryList = mutableListOf<String>()

        // Datei aus den Ressourcen laden
        val inputStream: InputStream? = object {}.javaClass.getResourceAsStream(resourcePath)
        if (inputStream != null) {
            try {
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0) // Die erste Tabelle im Dokument

            // Alle Zeilen durchlaufen
            for (row in sheet) {
                val cell = row.getCell(0) // Erste Zelle in der Zeile (Spalte A)
                if (cell != null) {
                    val countryName = cell.stringCellValue
                    logger.debug("Found Country: $countryName")
                    countryList.add(countryName)
                }
            }
            workbook.close()
        } catch (e: Exception) {
        logger.error("Error loading the file: $resourcePath", e)
    }
        } else {
            logger.error("The File $resourcePath could not be found.")
        }

        logger.info("Countries successfully loaded: ${countryList.size} Entries found.")
        return countryList
    }

}