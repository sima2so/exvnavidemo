package com.example.navi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.app.Activity
import androidx.appcompat.widget.Toolbar

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.navi.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val buttonSetRoute = findViewById<Button>(R.id.button_SetRoute)
////        val buttonStartNavi = findViewById<Button>(R.id.button_StartNavi)
//        //ここから遷移用のコード
//        buttonSetRoute.setOnClickListener{
//            val intent = Intent(this,SearchGoalActivity::class.java)
//            startActivity(intent)
//        }

//        //ここから遷移用のコード
//        buttonStartNavi.setOnClickListener{
//            val intent = Intent(this,NaviActivity::class.java)
//            startActivity(intent)
//        }
    }
}