package com.helloworld.mobilitycontainerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

  }

  fun onClick(v: View){
    Toast.makeText(this,"hello！",Toast.LENGTH_SHORT).show()
  }
}