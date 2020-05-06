package com.jstudio.app.entity

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName(value = "doctorName", alternate = ["name"]) val doctorName: String?,
    @SerializedName(value = "departmentName", alternate = ["depName, deptName"]) val departmentName: String?,
    val hospitalName: String?,
    val doctorId: Long,
    val depId: Long,
    val hospitalId: Long,
    val token: String
)