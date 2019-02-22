package com.example.review.view.main

interface MainPresenter {

    fun initView(view: MainView)
    fun getReview()
    fun createReview ()
}