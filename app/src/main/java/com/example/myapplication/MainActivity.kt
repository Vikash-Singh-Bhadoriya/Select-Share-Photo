package com.example.myapplication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    image_view.setImageURI(imageUri)
                } else {
                    Toast.makeText(this@MainActivity, "Can't get Photo", Toast.LENGTH_SHORT).show()
                }
            }

        select_photo.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }.also {
                // Verify the original intent will resolve to at least one activity
                try {
                    resultLauncher.launch(it)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this@MainActivity,
                        "No app available to get Photo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}