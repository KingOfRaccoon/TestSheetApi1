package com.example.testsheetapi

import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.util.*
import kotlin.jvm.Throws


class SheetsQuickstart {
    val APPLICATION_NAME = "Google Sheets API Java Quickstart"
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    private val TOKENS_DIRECTORY_PATH = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES: List<String> =
        Collections.singletonList(SheetsScopes.SPREADSHEETS)
    private val CREDENTIALS_FILE_PATH = "src/credentials.json"

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential? {
        val file = File("app/src/credentials.json")
        println(file.absolutePath)
        println(file.exists())
        // Load client secrets.
//        val `in` =
//            SheetsQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
//                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(file.inputStream()))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
//        val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
//        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        return null
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun main() {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        val spreadsheetId = "11iU3ca0EUFqKTPcufPuXEJnmZ5syItka_1IFYZz_07I"
        val range = "A2:B2"
        val service =
                Sheets.Builder(HTTP_TRANSPORT, SheetsQuickstart().JSON_FACTORY, SheetsQuickstart().getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(SheetsQuickstart().APPLICATION_NAME)
                        .build()
        val response: ValueRange = service.spreadsheets().values()[spreadsheetId, range]
                .execute()
        val values: List<List<Any>> =
                response.getValues()
        val value = listOf<List<Any>>(listOf("1234", "123", "123434"), listOf("0123", "12345", "1234"))
        val data: MutableList<ValueRange> =
                ArrayList()
        data.add(
                ValueRange()
                        .setValues(value)
        )
        val body = ValueRange()
                .setValues(value)

        if (values.isEmpty()) {
            println("No data found.")
        } else {
            println("Name, Major")
            for (row in values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                println(row.toString())
            }
            println(values.toString())
        }

        val addData = service.spreadsheets().values().update(spreadsheetId, "A3:C4", body)
                .setValueInputOption("RAW")
                .execute()
        println(addData.updatedCells)
    }
}
