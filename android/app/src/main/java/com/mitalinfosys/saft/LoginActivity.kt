package com.mitalinfosys.saft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    
    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    
    private val BASE_URL = "http://app.mitalinfosys.info/api/"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        
        // Test credentials
        etMobile.setText("7874783868")
        etPassword.setText("123456")
        
        btnLogin.setOnClickListener {
            val mobile = etMobile.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (mobile.length != 10) {
                Toast.makeText(this, "Enter 10-digit mobile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            loginUser(mobile, password)
        }
    }
    
    private fun loginUser(mobile: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = "${BASE_URL}android_login.php?mobile=$mobile&password=$password"
                
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                withContext(Dispatchers.Main) {
                    if (responseBody != null) {
                        handleLoginResponse(responseBody)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun handleLoginResponse(response: String) {
        try {
            val json = JSONObject(response)
            val success = json.getBoolean("success")
            
            if (success) {
                val user = json.getJSONObject("user")
                Toast.makeText(this, "Welcome ${user.getString("name")}", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_DATA", response)
                startActivity(intent)
                finish()
            } else {
                val message = json.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Server error", Toast.LENGTH_SHORT).show()
        }
    }
}