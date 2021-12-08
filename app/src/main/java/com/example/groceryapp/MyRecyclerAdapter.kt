package com.example.groceryapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

// LW Change to ArrayList
class MyRecyclerAdapter(private val myDataset: ArrayList<GroceryItem>, private val context: Context) :
    RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {

    // LW change from TextView to view
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyRecyclerAdapter.MyViewHolder {
        // LW create a new view, use the layout,   change TextView to View
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_layout, parent, false) as View
        // LW set the view's size, margins, paddings and layout parameters
        val lp = view.layoutParams
        lp.height = parent.measuredHeight/5
        view.layoutParams = lp
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem).text = myDataset[position].name + "\n$" + myDataset[position].price.toString() + "\nQuantity: " + myDataset[position].quantity.toString() + "\nTotal: " + (myDataset[position].price * myDataset[position].quantity).toString()

        val uri = context.resources.getIdentifier(context.packageName+":drawable/"+myDataset[position].image, null, null)
        //val uri = context.resources.getIdentifier(context.packageName+":drawable/goldfish_crackers_ch", null, null)
        holder.view.findViewById<ImageView>(R.id.imageViewRecyclerItem).setImageResource(uri)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}