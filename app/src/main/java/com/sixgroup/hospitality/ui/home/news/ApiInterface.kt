package com.sixgroup.hospitality.ui.home.news

import com.sixgroup.hospitality.utils.NEWS
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("top-headlines?country=id&category=health&apiKey=$NEWS")
    fun getStatus(): Call<Status>
}