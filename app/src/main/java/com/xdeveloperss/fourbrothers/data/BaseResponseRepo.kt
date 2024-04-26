package com.xdeveloperss.fourbrothers.data

import com.xdeveloperss.fourbrothers.data.responses.Data

data class BaseResponseRepo (
     var success: Boolean = false,
     var message: String = "Unknown Error please try after few minutes!",
     var errorCode: Int?=null,
     var data: Data? = null,
)