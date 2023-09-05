package com.inspiredcoda.ussddialer.model

import androidx.annotation.DrawableRes

data class UssdCode(
    val title: String,
    val ussdCode: String,
    @DrawableRes val icon: Int
)
