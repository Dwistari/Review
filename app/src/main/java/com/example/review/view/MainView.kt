package com.example.review.view

import com.example.review.data.Review

interface MainView {

    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(review: MutableList<Review>)
}