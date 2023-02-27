package com.example.frameapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class NewProjectScreen : AppCompatActivity() {
    lateinit var openProject_bt : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project_screen)

        findIDs()
        listeners()
    }

    private fun listeners() {
        openProject_bt.setOnClickListener {
            var intent = Intent(this,ProjectDashboard::class.java)
            startActivity(intent)
        }
    }

    private fun findIDs() {
        openProject_bt = findViewById(R.id.openProject_bt)
    }
}