package me.yokeyword.fragmentation.helper.internal

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @Hide
 * Result 记录
 * Created by YoKeyword on 16/6/2.
 */
@Parcelize
data class ResultRecord(
    var requestCode: Int = 0,
    var resultCode: Int = 0,
    var resultBundle: Bundle? = null
) : Parcelable