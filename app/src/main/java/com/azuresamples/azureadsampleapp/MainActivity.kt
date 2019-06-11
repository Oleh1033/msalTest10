package com.azuresamples.azureadsampleapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.AuthenticationResult
import com.microsoft.identity.client.PublicClientApplication
import kotlinx.android.synthetic.main.activity_main.*
import com.microsoft.identity.client.exception.MsalServiceException
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException

class MainActivity : AppCompatActivity() {

    var SCOPES = arrayOf("https://graph.microsoft.com/.default")

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
              //      val a = authenticationResult?.accessToken

                                         textView2.text = authenticationResult?.expiresOn?.toString()
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

}