package com.example.testsheetapi

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class PostData: AppCompatActivity() {
    private val progress: ProgressDialog? = null


    lateinit var tvName: EditText
    lateinit var tvCountry: EditText
    lateinit var button: Button
    lateinit var name: String
    lateinit var country: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.btn_submit)
        tvName = findViewById(R.id.input_name)
        tvCountry = findViewById(R.id.input_country)

        button.setOnClickListener {
            name = tvName.text.toString()
            country = tvCountry.text.toString()
            SendRequest().execute()
        }
    }

    inner class SendRequest : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {}
        override fun doInBackground(vararg params: String?): String {
            try {
                //Change your web app deployed URL or u can use this for attributes (name, country)
                val url =
                    URL("https://script.google.com/macros/s/AKfycbz0keSwr2EKCPVdYUT8TjYY_5JgjoPNZlLC3yhfbssIysWn2p1g/exec")
                val postDataParams = JSONObject()

                val id = "11iU3ca0EUFqKTPcufPuXEJnmZ5syItka_1IFYZz_07I"
                postDataParams.put("a", name)
                postDataParams.put("b", country)
                postDataParams.put("id", id)
                Log.e("params", postDataParams.toString())
                val conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 15000
                conn.connectTimeout = 15000
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true
                val os = conn.outputStream
                val writer = BufferedWriter(
                    OutputStreamWriter(os, "UTF-8")
                )
                writer.write(getPostDataString(postDataParams))
                writer.flush()
                writer.close()
                os.close()
                val responseCode = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                    val sb = StringBuffer("")
                    var line: String? = ""
                    while (`in`.readLine().also { line = it } != null) {
                        sb.append(line)
                        break
                    }
                    `in`.close()
                    return sb.toString()
                } else {
                    return ("false : $responseCode")
                }
            } catch (e: Exception) {
                return ("Exception: " + e.message)
            }
        }
        fun getPostDataString(params: JSONObject): String {
            val result = StringBuilder()
            var first = true
            val itr = params.keys()
            while (itr.hasNext()) {
                val key = itr.next()
                val value = params[key]
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
            return result.toString()
        }
    }
}