package com.xdeveloperss.fourbrothers.xnetwork.config.models

import com.xdeveloperss.fourbrothers.data.UserData

data class AuthResponse(var success:Boolean = false, var message: String? = null,
                        var token: String? = null, var data: UserData? = null)
