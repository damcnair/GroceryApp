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

class ImageScanner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_scanner)
    }

    fun onButtonClick(view: View) {
        val myBitmap: Bitmap =
//            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.chair)
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.orange)

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