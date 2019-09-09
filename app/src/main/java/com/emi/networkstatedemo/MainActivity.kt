package com.emi.networkstatedemo

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : BaseActivity() {

    private lateinit var restApi : RestNetwork
    private val compositeDisposable = CompositeDisposable()
    private var disposable = Disposables.empty()
    private lateinit var adapter : DrinkAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private var isNetworkLoading = MutableLiveData<Boolean>()
    private val isloading : LiveData<Boolean>
    get() =  isNetworkLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = DrinkAdapter(mutableListOf(), this)
        rec_drink.layoutManager = staggeredGridLayoutManager
        rec_drink.adapter = adapter
        rec_drink.setHasFixedSize(true)
        restApi = RestNetwork(this)
        fetchData()

        isloading.observe(this, Observer {
            if(it != null){
                retry_button.visibility = View.VISIBLE
                displayMessage.visibility = View.VISIBLE
                swipeContainer.isRefreshing = true
                retry_button.setOnClickListener {
                    fetchData()
                    retry_button.visibility = View.GONE
                    displayMessage.visibility  = View.GONE
                    displayErrorDialog(getString(R.string.noInternet), getString(R.string.noresponse_detail))
                }
            }else{
                swipeContainer.isRefreshing = false
                swipeContainer.setOnRefreshListener {
                    fetchData()
                }

            }
        })

    }

    fun fetchData(){
        disposable = restApi.networkProvider().fetchingWines()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cache()
            .cacheWithInitialCapacity(100)
            .doOnComplete {
                Log.d(MainActivity::class.java.simpleName, "successful response")
            }
            .subscribe(this::onResponseSuccess, this::onResponseError)
           compositeDisposable.add(disposable)
    }


   private fun onResponseSuccess(response : CocktailResponse?){
       if(response != null){
           try {
               val result : MutableList<Cocktails>? = response.result.toMutableList()
               isNetworkLoading.value = null
               result.let {
                   if(it != null){
                    adapter.updateDrinks(it)
                    adapter.notifyDataSetChanged()
                   }
               }
           }catch (e : Exception){
               e.printStackTrace()
               isNetworkLoading.value = true
               Log.d(MainActivity::class.java.simpleName, "${e.printStackTrace()}")
           }

       }
    }

   private fun onResponseError(throwable: Throwable){
       Log.d(MainActivity::class.java.simpleName, "${throwable.printStackTrace()}")
       isNetworkLoading.value = true
    }

    override fun onStop() {
        super.onStop()
    }
}
