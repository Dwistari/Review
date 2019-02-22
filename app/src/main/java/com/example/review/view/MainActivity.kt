package com.example.review.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.review.R
import com.example.review.data.model.Review
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), MainView {
    private lateinit var adapter: MainAdapter
    private lateinit var presenter: MainPresenter

    override fun showLoading() {
        loadings.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loadings.visibility = View.GONE
    }


    override fun showErrorAlert(it: Throwable) {
        Log.e("Main", it.localizedMessage)
        Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(review: MutableList<Review>)
    {
        adapter.setData(review)
//        swipe_refresh?.isRefreshing = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecylerView()
        initPresenter()
        presenter.getReview()

    }

    private fun initRecylerView() {

        card_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter()
        card_recycler_view.adapter = adapter
    }
    private fun initPresenter() {
        presenter = MainPresenterImp()
        presenter.initView(this)
    }

}