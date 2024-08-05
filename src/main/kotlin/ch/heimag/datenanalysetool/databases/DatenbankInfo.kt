package ch.heimag.datenanalysetool.databases

interface DatenbankInfo {
    val PROTOCOL: String
    val HOST: String
    val PORT: Int
    val DATABASE: String
    val OPTIONS: String
    val URL: String
    val USER: String
    val PASSWORD: String
}