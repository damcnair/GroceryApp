package com.example.groceryapp

import java.io.Serializable

data class Customer(var fname: String = "", var lname: String = "", var address: String = "", var cc: String = ""):Serializable{}
