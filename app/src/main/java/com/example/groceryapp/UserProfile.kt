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


class UserProfile : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
        val inputfname : EditText = findViewById(R.id.inputfname) as EditText
        val inputlname : EditText = findViewById(R.id.inputlname) as EditText
        val inputaddress : EditText = findViewById(R.id.inputAddress) as EditText
        val inputcc : EditText = findViewById(R.id.inputcc) as EditText
        val customer:ArrayList<Customer> = intent.getSerializableExtra("customer") as ArrayList<Customer>
        for (c in customer){
            inputfname.setText(c.fname)
            inputlname.setText(c.lname)
            inputaddress.setText(c.address)
            inputcc.setText(c.cc)
        }



    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                finish()
            }
        }
    }
}