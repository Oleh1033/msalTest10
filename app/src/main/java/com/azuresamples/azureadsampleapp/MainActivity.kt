package com.azuresamples.azureadsampleapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.azuresamples.azureadsampleapp.azureadsampleapp.Kudos
import com.azuresamples.azureadsampleapp.azureadsampleapp.TestModel
import com.google.gson.Gson
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.AuthenticationResult
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    var SCOPES = arrayOf("https://graph.microsoft.com/.default")
    //lateinit var kudosList : List<Kudos>
    lateinit var kudosListString : String

    var myApp: PublicClientApplication? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        myApp = PublicClientApplication(
                this.applicationContext,
                "27ab5703-f9ac-46d0-a328-0489ec91b235")

        callGraph.setOnClickListener {
            myApp?.acquireToken(this, SCOPES, object: AuthenticationCallback{
                override fun onSuccess(authenticationResult: AuthenticationResult?) {
                    val a = authenticationResult?.accessToken.toString()
                    fetchBooks (a)
              //      authenticationResult.account
                    textView2.text = authenticationResult?.accessToken?.toString()

                }

                override fun onCancel() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }

                override fun onError(exception: MsalException?) {
                    if (exception is MsalClientException) {
                        /* Exception inside MSAL, more info inside MsalError.java */
                    } else if (exception is MsalServiceException) {
                        /* Exception when communicating with the STS, likely config issue */
                    }
                }

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        myApp?.handleInteractiveRequestRedirect(requestCode,resultCode, data)
    }

    fun fetchBooks (token: String) {
        println("fetching books")

        val url = "https://bicoolback.azurewebsites.net/api/kudos"
        val request = Request.Builder()
                .url(url)
                .addHeader("token", token)
                .build()

//        println(request)
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                //println(body)
                kudosListString = body.toString()
               // println(kudosListString)

                var gson = Gson()
                var jsonString = kudosListString
               // println(jsonString)
                var testModel = gson.fromJson(jsonString, Array<TestModel>::class.java)
               println(testModel[1].toString())
                var testModel2 = gson.toJson(testModel[1])
                println(testModel2)


            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
                e?.printStackTrace()
            }
        })
    }

}