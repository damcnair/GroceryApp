package com.example.groceryapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.ImageView
import android.widget.Button
import java.io.IOException
import android.net.Uri
import android.provider.MediaStore

private val pickImage = 100
private var imageUri: Uri? = null

private lateinit var imageImage: ImageView
class ImageScanner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_scanner)

        imageImage = findViewById(R.id.imageView2) as ImageView
        val uploadButton: Button = findViewById(R.id.button) as Button
        uploadButton.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            var uriBitmap:Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            imageImage.setImageBitmap(uriBitmap)



        }
    }
    fun onButtonClick(view: View) {
        val imageImage: ImageView = findViewById(R.id.imageView2) as ImageView
        imageImage.buildDrawingCache()

        var myBitmap:Bitmap = imageImage.getDrawingCache()
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
                        textItem.text =
                            "Found item: ${item!!.name}\nPrice: $${item!!.price}\nIn Stock: ${item!!.quantity}"
                    }
                }
                if (!foundItemInList) {
                    Toast.makeText(
                        this,
                        "Sorry we do not sell that type of item!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}