package com.example.review.data

import android.app.DownloadManager
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResponse(

    @SerializedName("data")
    @Expose
    var review:  MutableList<Review>
)

data class Review(

    @SerializedName("id")
    @Expose
    var id : String,
    @SerializedName("name")
    @Expose
    var name :  String ,
    @SerializedName("description")
    @Expose
    var description: String,
    @SerializedName("auth")
    @Expose
    var auth: Any ,
    @SerializedName("events")
    @Expose
    var events : Any?,
    @SerializedName("variables")
    @Expose
    var variables: List<Any>,
    @SerializedName("order")
    @Expose
    var order: List<String>,
    @SerializedName("folders_order")
    @Expose
    var foldersOrder : List<Any>,
    @SerializedName("folders")
    @Expose
    var folders : List<Any>,
    @SerializedName("requests")
    @Expose
    var requests : List<Request>

)

data class Request (

    @SerializedName("id")
    @Expose
    var id: String?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("url")
    @Expose
    var url: String?,
    @SerializedName("description")
    @Expose
    var description: String? ,
    @SerializedName("data")
    @Expose
    var data: Any? ,
    @SerializedName("dataMode")
    @Expose
    var dataMode: Any?,
    @SerializedName("headerData")
    @Expose
    var headerData: Any?,
    @SerializedName("method")
    @Expose
    var method: String? ,
    @SerializedName("pathVariableData")
    @Expose
    var pathVariableData: List<Any>? ,
    @SerializedName("queryParams")
    @Expose
    var queryParams: List<Any>?,
    @SerializedName("auth")
    @Expose
    var auth: Any? ,
    @SerializedName("events")
    @Expose
    var events: Any?,
    @SerializedName("folder")
    @Expose
    var folder: Any? ,
    @SerializedName("currentHelper")
    @Expose
    var currentHelper: Any?,
    @SerializedName("helperAttributes")
    @Expose
    var helperAttributes: Any?,
    @SerializedName("collectionId")
    @Expose
    var collectionId: String?,
    @SerializedName("pathVariables")
    @Expose
    var pathVariables: List<Any>?,
    @SerializedName("headers")
    @Expose
    var headers: String?

    )