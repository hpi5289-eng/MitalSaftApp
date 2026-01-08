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
        
        // Test data
        etMobile.setText("7874783868")
        etPassword.setText("123456")
        
        btnLogin.setOnClickListener {
            val mobile = etMobile.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (mobile.length != 10) {
                showToast("10-digit mobile dalo")
                return@setOnClickListener
            }
            
            if (password.isEmpty()) {
                showToast("Password dalo")
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
                        try {
                            val json = JSONObject(responseBody)
                            val success = json.getBoolean("success")
                            
                            if (success) {
                                val user = json.getJSONObject("user")
                                val userName = user.getString("name")
                                
                                showToast("Welcome $userName")
                                
                                // Go to Dashboard
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("USER_DATA", responseBody)
                                startActivity(intent)
                                finish()
                                
                            } else {
                                val message = json.getString("message")
                                showToast(message)
                            }
                        } catch (e: Exception) {
                            showToast("Server error")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Network error")
                }
            }
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}