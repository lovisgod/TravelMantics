package com.lovisgod.travelmantics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelDeal(
    val id:String = "",
    val title:String = "",
    val description:String = "",
    val price: String= "",
    val imageUrl:String= ""
) : Parcelable