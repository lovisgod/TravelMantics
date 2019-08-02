package com.lovisgod.travelmantics

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelDeals(
    val title:String = "",
    val description:String = "",
    val price: String= "",
    val imageUrl:String= ""
) : Parcelable


