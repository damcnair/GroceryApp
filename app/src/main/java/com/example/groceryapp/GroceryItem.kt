package com.example.groceryapp

import com.google.gson.reflect.TypeToken

data class GroceryItem(val id:Int =0, val name:String ="", val price:Float =0.0f, val quantity:Int =0, val image:String ="", val barcode:String) {
    companion object {
        var groceryStore: ArrayList<GroceryItem> = ArrayList()
        var groceryList: ArrayList<GroceryItem> = ArrayList()
    }
}