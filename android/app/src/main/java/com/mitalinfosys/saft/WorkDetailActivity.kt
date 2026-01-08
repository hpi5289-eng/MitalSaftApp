package com.mitalinfosys.saft

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WorkDetailActivity : AppCompatActivity() {
    
    private lateinit var tvPartyName: TextView
    private lateinit var tvWorkName: TextView
    private lateinit var tvWorkId: TextView
    private lateinit var etRemark: EditText
    private lateinit var btnMarkDone: Button
    private lateinit var btnBack: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_detail)
        
        tvPartyName = findViewById(R.id.tvPartyName)
        tvWorkName = findViewById(R.id.tvWorkName)
        tvWorkId = findViewById(R.id.tvWorkId)
        etRemark = findViewById(R.id.etRemark)
        btnMarkDone = findViewById(R.id.btnMarkDone)
        btnBack = findViewById(R.id.btnBack)
        
        val workId = intent.getStringExtra("WORK_ID") ?: "#00"
        val partyName = intent.getStringExtra("PARTY_NAME") ?: "Unknown"
        val workName = intent.getStringExtra("WORK_NAME") ?: "Unknown"
        
        tvPartyName.text = "Party: $partyName"
        tvWorkName.text = "Work: $workName"
        tvWorkId.text = "Work ID: $workId"
        
        btnMarkDone.setOnClickListener {
            val remark = etRemark.text.toString().trim()
            if (remark.isEmpty()) {
                Toast.makeText(this, "Enter remark", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            markWorkDone(workId, remark)
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun markWorkDone(workId: String, remark: String) {
        Toast.makeText(this, "Work $workId completed!", Toast.LENGTH_SHORT).show()
        finish()
    }
}