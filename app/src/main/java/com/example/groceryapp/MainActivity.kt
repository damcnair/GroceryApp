package com.example.groceryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jsonFileString = getJSONDataFromAsset(this, "grocery_list.json")
        //TypeToken to get the type of the object
        val listGroceries = object : TypeToken<ArrayList<GroceryItem>>() {}.type
        GroceryItem.groceryList = Gson().fromJson(jsonFileString, listGroceries)

        val jsonFileStringStore = getJSONDataFromAsset(this, "items.json")
        //TypeToken to get the type of the object
        val items = object : TypeToken<ArrayList<GroceryItem>>() {}.type
        GroceryItem.groceryStore = Gson().fromJson(jsonFileString, items)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyRecyclerAdapter(GroceryItem.groceryList, this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
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
        when(view.id) {
            R.id.buttonImage->{

                }

            R.id.buttonBarcode->{

                }
            R.id.buttonProfile->{
                val intent = Intent(this, UserProfile::class.java).apply {}
                startActivity(intent)

                }
        }
    }



}