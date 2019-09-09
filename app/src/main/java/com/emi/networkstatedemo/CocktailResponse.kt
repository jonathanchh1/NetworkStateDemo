package com.emi.networkstatedemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CocktailResponse(@Expose @SerializedName("drinks") var result : ArrayList<Cocktails>)