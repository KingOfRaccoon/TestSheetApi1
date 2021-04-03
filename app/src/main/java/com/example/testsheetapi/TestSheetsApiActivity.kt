package com.example.testsheetapi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TestSheetsApiActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_SIGN_IN = 1
    }

    val SIGN_IN_CODE = 7


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestSignIn(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val scopes = listOf(SheetsScopes.SPREADSHEETS)
                    val credential = GoogleAccountCredential.usingOAuth2(this, scopes)
                    credential.selectedAccount = it.result?.account

                    val jsonFactory = JacksonFactory.getDefaultInstance()
                    // GoogleNetHttpTransport.newTrustedTransport()
                    val httpTransport = AndroidHttp.newCompatibleTransport()
                    val service = Sheets.Builder(httpTransport, jsonFactory, credential)
                            .setApplicationName(getString(R.string.app_name))
                            .build()
                    Log.e("e", httpTransport.toString())
                    createSpreadsheet(service)
                }
            }
        }
    }


    private fun requestSignIn(context: Context) {
        /*
        GoogleSignIn.getLastSignedInAccount(context)?.also { account ->
            Timber.d("account=${account.displayName}")
        }
         */
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestEmail()
                // .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
                .build()
        val client = GoogleSignIn.getClient(context, signInOptions)
        startActivityForResult(Intent(client.signInIntent), SIGN_IN_CODE)

    }

    private fun createSpreadsheet(service: Sheets) {
        var spreadsheet = Spreadsheet()
                .setProperties(
                        SpreadsheetProperties()
                                .setTitle("CreateNewSpreadsheet")
                )

        MainScope().launch(Dispatchers.IO) {
            spreadsheet = service.spreadsheets().create(spreadsheet).execute()
            Log.d("id", "ID: ${spreadsheet.spreadsheetId}")
        }
    }
}