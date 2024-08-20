
package ch.heimag.datenanalysetool.country

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class Country {

    fun loadToSelectCountry(resourcePath: String): MutableList<String> {
        val countryList = mutableListOf<String>()

        // Datei aus den Ressourcen laden
        val inputStream: InputStream? = object {}.javaClass.getResourceAsStream(resourcePath)
        if (inputStream != null) {
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0) // Die erste Tabelle im Dokument

            // Alle Zeilen durchlaufen
            for (row in sheet) {
                val cell = row.getCell(0) // Erste Zelle in der Zeile (Spalte A)
                if (cell != null) {
                    val countryName = cell.stringCellValue
                    countryList.add(countryName)
                }
            }
            workbook.close()
        }
        return countryList
    }

}