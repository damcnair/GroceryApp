package com.example.groceryapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import android.content.Intent
import android.widget.ImageView
import android.widget.Button
import android.net.Uri
import android.provider.MediaStore
import android.graphics.drawable.AnimationDrawable
import java.text.NumberFormat
import java.util.*

private val pickImage = 100
private var imageUri: Uri? = null
private lateinit var frameAnimation: AnimationDrawable
private lateinit var scanButton: Button
private lateinit var imageView: ImageView

class ImageScanner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_scanner)
        val img: ImageView = findViewById<ImageView>(R.id.imgLoad2)
        img.setBackgroundResource(R.drawable.loading)
        frameAnimation = img.background as AnimationDrawable
        frameAnimation.start()


        scanButton = findViewById<Button>(R.id.btnScanImage)
        val waitLabel: TextView = findViewById<TextView>(R.id.textIWait2)
        imageView = findViewById<ImageView>(R.id.imageViewTextScanner)
        val uploadButton: Button = findViewById<Button>(R.id.button)

        uploadButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            waitLabel.setText(R.string.wait_label_image_scan)
            uploadButton.setEnabled(false)
            img.setVisibility(View.GONE)
            scanButton.setEnabled(true)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            var uriBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)

            imageView.setImageBitmap(uriBitmap)


        }
    }

    fun onButtonClick(view: View) {
        val imageImage: ImageView = findViewById(R.id.imageViewTextScanner) as ImageView
        imageImage.buildDrawingCache()

        var myBitmap: Bitmap = imageImage.getDrawingCache()
        //get item textview
        val textItem: TextView = findViewById<TextView>(R.id.txtViewScannedItemFound)
        //get InputImage
        val image = InputImage.fromBitmap(myBitmap, 0)

        //set up image labeler
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()

        val labeler = ImageLabeling.getClient(options)
//        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                var foundItemInList = false
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    println("Item = $text; Confidence = $confidence")
                    if (GroceryItem.groceryStore.find { item ->
                            item.name.equals(
                                label.text,
                                ignoreCase = true
                            )
                        } != null) {
                        //item found!
                        foundItemInList = true
                        val item = GroceryItem.groceryStore.find { item ->
                            item.name.equals(
                                label.text,
                                ignoreCase = true
                            )
                        }
                        println("found item ${label.text}")

                        textItem.text = getString(
                            R.string.image_scan_product_text,
                            item!!.name,
                            NumberFormat.getCurrencyInstance(Locale.getDefault())
                                .format(item!!.price),
                            item!!.quantity
                        )
                    }
                }
                if (!foundItemInList) {
                    Toast.makeText(
                        this,
                        R.string.image_scan_toast_text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}