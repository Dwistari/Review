package com.example.review.view

import com.example.jogjatour.data.ReviewInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenterImp : MainPresenter {

    private lateinit var view: MainView
    private var interactor: ReviewInteractor = ReviewInteractor()
    private var disposables = CompositeDisposable()

    override fun getReview() {
        view.showLoading()
        interactor.getReview()
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showData(it.review)
                },
                {
                    view.showErrorAlert(it)
                    view.dismissLoading()
                }
            )?.let {
                disposables.add(
                    it
                )
            }
    }
    override fun initView(view: MainView) {
        this.view = view
    }
}