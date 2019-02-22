package com.example.jogjatour.data

import com.example.jogjatour.data.api.ApiServiceInterface
import com.example.review.data.ReviewResponse
import io.reactivex.Observable

class ReviewInteractor {

    fun getReview ( ): Observable <ReviewResponse> {
        return api.getReview ()
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}