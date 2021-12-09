package com.example.groceryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
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

class BarcodeScanner:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcode_scanner)

        var myBitmap: Bitmap = BitmapFactory.decodeResource(
            getApplicationContext().getResources(),
            R.drawable.barcode2
        );
        var textStatus: TextView = findViewById(R.id.txtStatus) as TextView

        // put the bitmap into the InputImage
        val image = InputImage.fromBitmap(myBitmap, 0)
        // Set up the barcode scanner
        val scanner = BarcodeScanning.getClient()

        // processing the image
        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                textStatus.setText("Found Barcode: "+barcodes[0].rawValue)

                // Task completed successfully
            }
            .addOnFailureListener {
                // Task failed with an exception
                textStatus.setText("Failed to read the barcode")
            }

    }


}