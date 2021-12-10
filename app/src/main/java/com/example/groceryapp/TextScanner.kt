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
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

private val pickImage = 100
private var imageUri: Uri? = null

private lateinit var imageImage: ImageView
class TextScanner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_scanner)

        imageImage = findViewById(R.id.imageView2) as ImageView
        val uploadButton: Button = findViewById(R.id.button) as Button
        uploadButton.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    fun onButtonClick(view: View) {
        val imageImage: ImageView = findViewById(R.id.imageView2) as ImageView
        imageImage.buildDrawingCache()

        var myBitmap: Bitmap = imageImage.getDrawingCache()
        //get item textview
        val textItem: TextView = findViewById<TextView>(R.id.txtViewScannedItemFound)
        //get InputImage
        val image = InputImage.fromBitmap(myBitmap, 0)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                var foundItemInList = false
                for (block in visionText.textBlocks) {
                    val text = block.text
                    println("Item = $text")
                    if (GroceryItem.groceryStore.find { item ->
                            text.contains(item.name )
                    } != null) {
                        //item found!
                        foundItemInList = true
                        val item = GroceryItem.groceryStore.find { item ->
                            text.contains(item.name )
                        }
                        if (item != null) {
                            println("found item ${item.name}")
                        }
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