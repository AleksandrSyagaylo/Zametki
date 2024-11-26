package com.example.myzametki1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val addbut: TextView = findViewById(R.id.addbut)
        addbut.setOnClickListener {
            startActivity(Intent(this@MainActivity,addreport :: class.java))

        }
        var h = Handler()
        var db = Firebase.firestore
        db.addSnapshotsInSyncListener {
            updateBD()
        }
        h.postDelayed({ updateBD() }, 200)
    }

    private fun updateBD() {
        var recyclerView: RecyclerView = findViewById(R.id.recycleview)
        var db = Firebase.firestore
        db.collection("reports").get().addOnSuccessListener {
            val reportList = mutableListOf<CustomModel>()
            for (doc in it) {
                val report = CustomModel(
                    doc.id, doc.getString("name").toString(), doc.getString("desctext").toString(),
                    doc.getLong("date")!!.toInt()
                )
                reportList.add(report)
            }
            reportList.sortBy { it.date }
            reportList.reverse()
            val reportAdapter = CustomAdapter(this@MainActivity, reportList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = reportAdapter
        }.addOnFailureListener {
            Toast.makeText(this, "Не работает БД", Toast.LENGTH_LONG).show()
        }
    }
}