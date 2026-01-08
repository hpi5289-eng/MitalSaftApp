package com.mitalinfosys.saft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {
    
    private lateinit var tvUserName: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvPendingCount: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var btnLogout: Button
    
    private lateinit var worksAdapter: WorksAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        tvUserName = findViewById(R.id.tvUserName)
        tvMobile = findViewById(R.id.tvMobile)
        tvPendingCount = findViewById(R.id.tvPendingCount)
        recyclerView = findViewById(R.id.recyclerView)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnLogout = findViewById(R.id.btnLogout)
        
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        worksAdapter = WorksAdapter(mutableListOf()) { work ->
            // Work item clicked
            val intent = Intent(this, WorkDetailActivity::class.java)
            intent.putExtra("WORK_ID", work.workId)
            intent.putExtra("PARTY_NAME", work.partyName)
            intent.putExtra("WORK_NAME", work.workName)
            startActivity(intent)
        }
        recyclerView.adapter = worksAdapter
        
        // Load user data
        val userDataJson = intent.getStringExtra("USER_DATA")
        if (userDataJson != null) {
            displayUserData(userDataJson)
        }
        
        btnRefresh.setOnClickListener {
            showToast("Refreshing...")
            // Here you can call API to refresh data
        }
        
        btnLogout.setOnClickListener {
            finish() // Go back to login
        }
    }
    
    private fun displayUserData(jsonString: String) {
        try {
            val json = JSONObject(jsonString)
            val user = json.getJSONObject("user")
            
            tvUserName.text = "Staff: ${user.getString("name")}"
            tvMobile.text = "Mobile: ${user.getString("mobile")}"
            
            if (json.has("pending_count")) {
                val count = json.getInt("pending_count")
                tvPendingCount.text = "Pending Works: $count"
                
                // Load works list
                if (json.has("pending_works")) {
                    val worksArray = json.getJSONArray("pending_works")
                    val worksList = mutableListOf<Work>()
                    
                    for (i in 0 until worksArray.length()) {
                        val workObj = worksArray.getJSONObject(i)
                        worksList.add(Work(
                            workId = "#${41 - i}",
                            partyName = workObj.getString("party_name"),
                            workName = workObj.getString("work"),
                            date = "08-01-2026",
                            status = "Pending"
                        ))
                    }
                    
                    worksAdapter.updateList(worksList)
                }
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

// Data class for Work
data class Work(
    val workId: String,
    val partyName: String,
    val workName: String,
    val date: String,
    val status: String
)