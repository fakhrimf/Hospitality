package com.sixgroup.hospitality.utils.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatabaseMessageModel(
    val success: Boolean,
    val message: String
) : Parcelable