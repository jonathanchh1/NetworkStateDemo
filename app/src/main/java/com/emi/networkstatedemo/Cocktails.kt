package com.emi.networkstatedemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cocktails(@Expose @SerializedName("idDrink") var id : String ="",
                @Expose @SerializedName("strDrink") var name : String?=null,
                @Expose @SerializedName("strDrinkThumb") var thumbnail : String?=null)