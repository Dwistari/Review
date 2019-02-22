package com.example.review.view.main

import com.example.review.data.model.Review

interface MainView {

    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(review: MutableList<Review>)
}