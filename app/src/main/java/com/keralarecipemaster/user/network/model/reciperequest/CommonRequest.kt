package com.keralarecipemaster.user.network.model.reciperequest

import com.google.gson.annotations.SerializedName

data class CommonRequest(@SerializedName("user_id") val userId: Int)
