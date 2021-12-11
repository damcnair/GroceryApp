package com.example.groceryapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.NumberFormat
import java.util.*
import android.graphics.drawable.AnimationDrawable

private val pickImage = 100
private var imageUri: Uri? = null

private lateinit var imageView: ImageView
private lateinit var scanButton: Button
private lateinit var textView: TextView
private lateinit var uploadButton: Button

class TextScanner : AppCompatActivity() {
    private lateinit var frameAnimation :AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_scanner)
        scanButton = findViewById<Button>(R.id.btnScanImage)
        scanButton.isEnabled = false
        imageView = findViewById<ImageView>(R.id.imageViewTextScanner)

        textView = findViewById<TextView>(R.id.textItem3)
        textView.text = getString(R.string.text_scan_upload_image)
        uploadButton = findViewById<Button>(R.id.button)
        val img:ImageView = findViewById(R.id.imgLoad3) as ImageView
        img.setBackgroundResource(R.drawable.loading)
        frameAnimation = img.background as AnimationDrawable
        frameAnimation.start()
        uploadButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            textView.text = getString(R.string.wait_label_image_scan)

            uploadButton.isEnabled = false
            scanButton.isEnabled = true
            img.setVisibility(View.GONE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            var uriBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

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

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                var foundItemInList = false
                for (block in visionText.textBlocks) {
                    val text = block.text
                    println("Item = $text")
                    if (GroceryItem.groceryStore.find { item ->
//                            text.contains(item.name, ignoreCase = true)
                            item.name.contains(text, ignoreCase = true)
                        } != null) {
                        //item found!
                        foundItemInList = true
                        val item = GroceryItem.groceryStore.find { item ->
//                            text.contains(item.name, ignoreCase = true)
                            item.name.contains(text, ignoreCase = true)
                        }
                        if (item != null) {
                            println("found item ${item.name}")
                        }
                        textItem.text =
                            getString(
                                R.string.image_scan_product_text,
                                item!!.name,
                                NumberFormat.getCurrencyInstance(
                                    Locale.getDefault()
                                ).format(item!!.price),
                                item!!.quantity
                            )
                    }
                }
                if (!foundItemInList) {
                    Toast.makeText(
                        this,
                        getString(R.string.text_scan_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                    scanButton.isEnabled = false
                    uploadButton.isEnabled = true
                    imageView.setImageBitmap(null)
                    imageView.visibility = View.GONE
                    textView.text = getString(R.string.text_scan_upload_image)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}