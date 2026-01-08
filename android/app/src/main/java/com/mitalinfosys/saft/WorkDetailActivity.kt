package com.mitalinfosys.saft

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class WorkDetailActivity : AppCompatActivity() {
    
    private lateinit var tvPartyName: TextView
    private lateinit var tvWorkName: TextView
    private lateinit var tvWorkId: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var etRemark: EditText
    private lateinit var btnMarkDone: Button
    private lateinit var btnBack: Button
    
    private var workId = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_detail)
        
        tvPartyName = findViewById(R.id.tvPartyName)
        tvWorkName = findViewById(R.id.tvWorkName)
        tvWorkId = findViewById(R.id.tvWorkId)
        tvDate = findViewById(R.id.tvDate)
        tvStatus = findViewById(R.id.tvStatus)
        etRemark = findViewById(R.id.etRemark)
        btnMarkDone = findViewById(R.id.btnMarkDone)
        btnBack = findViewById(R.id.btnBack)
        
        // Get data from intent
        workId = intent.getStringExtra("WORK_ID") ?: "#00"
        val partyName = intent.getStringExtra("PARTY_NAME") ?: "Unknown"
        val workName = intent.getStringExtra("WORK_NAME") ?: "Unknown"
        
        // Display data
        tvPartyName.text = "Party: $partyName"
        tvWorkName.text = "Work: $workName"
        tvWorkId.text = "Work ID: $workId"
        tvDate.text = "Date: 08-01-2026"
        tvStatus.text = "Status: Pending"
        
        btnMarkDone.setOnClickListener {
            val remark = etRemark.text.toString().trim()
            if (remark.isEmpty()) {
                Toast.makeText(this, "Remark dalo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            markWorkDone(remark)
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun markWorkDone(remark: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = "http://app.mitalinfosys.info/api/mark_done.php"
                val params = "work_id=$workId&remark=${remark}"
                
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("$url?$params")
                    .post("".toRequestBody())
                    .build()
                
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                withContext(Dispatchers.Main) {
                    if (responseBody != null) {
                        try {
                            val json = JSONObject(responseBody)
                            val success = json.getBoolean("success")
                            
                            if (success) {
                                Toast.makeText(
                                    this@WorkDetailActivity,
                                    "Work completed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                val message = json.getString("message")
                                Toast.makeText(this@WorkDetailActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@WorkDetailActivity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WorkDetailActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}