package ch.heimag.datenanalysetool.country

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import org.slf4j.LoggerFactory

class Country {
  private  val logger = LoggerFactory.getLogger("FileReader.Country")

    fun loadToSelectCountry(resourcePath: String): MutableList<String> {
         logger.info("Lade Länder aus der Datei: $resourcePath")
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
                    logger.debug("Land gefunden: $countryName")
                    countryList.add(countryName)
                }
            }
            workbook.close()
        } catch (e: Exception) {
        logger.error("Fehler beim Laden der Datei: $resourcePath", e)
    }
        } else {
            logger.error("Die Datei $resourcePath konnte nicht gefunden werden.")
        }

        logger.info("Länder erfolgreich geladen: ${countryList.size} Einträge gefunden.")
        return countryList
    }

}