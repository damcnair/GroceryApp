package com.example.groceryapp

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

class BarcodeScanner:AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var result: GroceryItem = GroceryItem(0,"",0.0f, 0,"","")
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var barcodeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcode_scanner)
        barcodeImage = findViewById(R.id.imgBarcode) as ImageView
        val uploadButton: Button = findViewById(R.id.buttonUpload) as Button

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

            barcodeImage.setImageBitmap(uriBitmap)



        }
    }
    private fun getJSONDataFromAsset(context: Context, filename: String): String? {
        val jsonString: String
        try {
            // Use bufferedReader
            // Closable.use will automatically close the input at the end of execution
            // it.reader.readText()  is automatically
            jsonString = context.assets.open(filename).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun onButtonClick(view: View) {
        when (view.id) {


            R.id.buttonScan -> {
                val barcodeImage: ImageView = findViewById(R.id.imgBarcode) as ImageView
                barcodeImage.buildDrawingCache()

                var myBitmap:Bitmap = barcodeImage.getDrawingCache()
                var textStatus: TextView = findViewById(R.id.txtStatus) as TextView
                var textItem: TextView = findViewById(R.id.textItem) as TextView
                // put the bitmap into the InputImage
                val image = InputImage.fromBitmap(myBitmap, 0)
                // Set up the barcode scanner
                val scanner = BarcodeScanning.getClient()
                var barcodeString: String = ""

                // processing the image
                val result = scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        textStatus.setText("Found Barcode: " + barcodes[0].rawValue)
                        var barcodeString: String = barcodes[0].rawValue

                        result = returnItem(barcodeString)
                        println("FOUND" + result.name)
                        textItem.setText("Found Item: " + result.name+"\nPrice: $"+result.price +"\nIn Stock: "+result.quantity)
                        // Task completed successfully
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        textStatus.setText("Failed to read the barcode")
                    }


            }

        }
    }
    fun returnItem(b: String):GroceryItem{
        //Find item from grocery items and add to list? copied from main
        val jsonFileStringStore = getJSONDataFromAsset(this, "items.json")
        //TypeToken to get the type of the object
        val items = object : TypeToken<ArrayList<GroceryItem>>() {}.type
        GroceryItem.groceryStore = Gson().fromJson(jsonFileStringStore, items)

        var item: GroceryItem = GroceryItem(0,"",0.0f, 0,"","")

        println(b)
        for(i in GroceryItem.groceryStore.orEmpty()) {
            println(i.barcode)
            if (i.barcode == b) {
                item = i
            }
        }
        //println(item.barcode)
        return item
    }

}