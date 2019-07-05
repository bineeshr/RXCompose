package com.example.rxjavacompose

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val observable = Observable.fromCallable {
            mutableListOf("vinod", 1, "bineesh", 2, 3, 4, "rajesh", "manoj", 5, 4)
        }

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io()).compose(filterObject()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                print(it)
            })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun filterObject(): ObservableTransformer<MutableList<Any>, MutableList<String>> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { list ->
                val mutableList = mutableListOf<String>()
                list.forEach {
                    if (it is String) {
                        mutableList.add(it)
                    }
                }
                Observable.fromCallable { mutableList }
            }
        }

    }
}
