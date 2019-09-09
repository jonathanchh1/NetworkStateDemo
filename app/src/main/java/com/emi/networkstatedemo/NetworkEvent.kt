package com.emi.networkstatedemo

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject


object NetworkEvent{

    private lateinit var subject : PublishSubject<NetworkState>

    @SuppressLint("CheckResult")
    private fun getSubject() : PublishSubject<NetworkState>{
        if(!this::subject.isInitialized){
            subject = PublishSubject.create()
            subject.subscribeOn(AndroidSchedulers.mainThread())
        }
        return subject
    }

    private val compositeDisposableMap = HashMap<Any, CompositeDisposable>()


    private fun getCompositeSubscription(item : Any) : CompositeDisposable{
        var compositeSubscription : CompositeDisposable? = compositeDisposableMap[item]
          if(compositeSubscription == null){
            compositeSubscription = CompositeDisposable()
            compositeDisposableMap[item] = compositeSubscription
        }
        return compositeSubscription
    }


    fun publish(networkState: NetworkState){
        Handler(Looper.getMainLooper())
            .post {getSubject().onNext(networkState) }
    }


    fun register(lifecycle : Any, action : Consumer<NetworkState>){
        val disposable = getSubject().subscribe(action)
        getCompositeSubscription(lifecycle).add(disposable)
    }

    fun unregister(lifecycle: Any){
        val compositeSubscription : CompositeDisposable? = compositeDisposableMap.remove(lifecycle)
        compositeSubscription!!.dispose()
    }

}