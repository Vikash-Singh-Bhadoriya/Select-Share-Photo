package com.example.selectshare

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Image URI of the selected image that has to be share
    private var imageUri: Uri? = null

    //What to do when user select Image
    private val selectImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                image_view.setImageURI(imageUri)
                share_image_button.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.cannot_get_photo),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select_photo.setOnClickListener {
            selectPhoto()
        }

        share_image_button.setOnClickListener {
            shareImage()
        }
    }

    private fun selectPhoto() {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }.also {
            try {
                selectImageResultLauncher.launch(it)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.no_app_available_to_get_photo),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareImage() {
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
        }.also {
            try {
                startActivity(Intent.createChooser(it, getString(R.string.share_photo_with)))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.no_app_available_to_share_photo),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}