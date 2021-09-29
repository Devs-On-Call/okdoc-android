package com.devsoncall.okdoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import android.view.View.OnFocusChangeListener





class Login : AppCompatActivity() {

    private lateinit var backendApi: BackendApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login)

        //Retrofit init
        val retrofit = Retrofit.Builder()
            .baseUrl("https://okdoc-backend.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        backendApi = retrofit.create(BackendApi::class.java)

        val amkaField = findViewById<EditText>(R.id.editTextAmka)
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
        val loginImage = findViewById<View>(R.id.doctors_image)
        val activityRoot = findViewById<ConstraintLayout>(R.id.activity_root)

        amkaField.setOnFocusChangeListener { v, hasFocus ->
            Log.i("hasFocus", hasFocus.toString());
            if (hasFocus) {
                loginImage.setVisibility(View.GONE)
            } else {
                loginImage.setVisibility(View.VISIBLE)
            }
        }


        getStartedButton.setOnClickListener {
            Log.i("Listener", "Listener is running");
            createToken(amkaField.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createToken(amka: String) {
        var call: Call<Token> = backendApi.createToken(amka)
        val textViewResult = findViewById<TextView>(R.id.textViewLoginMessage)
        Log.i("Listener", "call has been created for amka " + amka);
        call.enqueue(
            object : Callback<Token> {
                override fun onResponse( call: Call<Token>, response: Response<Token>) {
                    Log.i("Response", "Response received");

                    if (!response.isSuccessful()) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        textViewResult.setText(jsonObj.getString("message"));
                        Log.i("Response", "Response not successful");
                        return
                    }

                    var token: Token? = response.body()

                    Log.i("Response", response.message());
                    textViewResult.setText(token?.message);
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.i("Response", "Response failed");
                    textViewResult.setText(t.localizedMessage);
                }
            }
        )
    }
}