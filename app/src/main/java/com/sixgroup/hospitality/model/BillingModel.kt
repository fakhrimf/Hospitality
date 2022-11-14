package com.sixgroup.hospitality.model

import java.util.Date

data class BillingModel(
    private val idBilling: String,
    private val billDate: Date,
    private val status: String
)
