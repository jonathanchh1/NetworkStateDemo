package com.emi.networkstatedemo

import io.reactivex.Observable
import retrofit2.http.GET

interface NetworkServices {

    @GET("filter.php?g=Cocktail_glass#drinks/7")
    fun fetchingWines() : Observable<CocktailResponse>
}