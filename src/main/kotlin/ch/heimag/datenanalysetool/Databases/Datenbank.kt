package ch.heimag.datenanalysetool.Databases

class Datenbank : DatenbankInfo {
    override val PROTOCOL = "jdbc:mysql"
    override val HOST = "localhost"
    override val PORT = 3306
    override val DATABASE = "heimag"
    override val OPTIONS = "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin"
    override val URL = "$PROTOCOL://$HOST:$PORT/$DATABASE?$OPTIONS"
    override val USER = "datenanalysetool"
    override val PASSWORD = "HeimAGS2we@!"

    /*
    fun checkLoginInformation(loginInformation: LoginInformation): vorhandeneBenutzer {
        var passwort = loginInformation.passwortWebseite


        val listeVorhandenerBenutzer = mutableListOf<String>()


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = "SELECT benutzer from heimag.login where passwort = '$passwort'"

        // SQL execute
        val data = statement.executeQuery(sql)

        // Output Message
        while (data.next()) {
            listeVorhandenerBenutzer.add(data.getString("benutzer"))
        }

        data.close()
        statement.close()
        connection.close()

        vorhandeneBenutzer.benutzer = listeVorhandenerBenutzer

        return vorhandeneBenutzer
    }

*/
}