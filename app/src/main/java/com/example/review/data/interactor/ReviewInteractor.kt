package com.example.jogjatour.data

import com.example.jogjatour.data.api.ApiServiceInterface
import com.example.review.data.model.ReviewResponse
import io.reactivex.Observable
import okhttp3.ResponseBody

class ReviewInteractor {

    fun getReview ( ): Observable <ReviewResponse> {
        return api.getReview ()
    }
    fun createReview ( ): Observable <ResponseBody> {
        return api.createReview ()
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}