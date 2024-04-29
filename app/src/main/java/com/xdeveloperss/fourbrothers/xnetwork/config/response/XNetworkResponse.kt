package com.xdeveloperss.fourbrothers.xnetwork.config.response

import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.WaitDialog

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
sealed class XNetworkResponse<out T>{
    data class Success<out T> (val value:T): XNetworkResponse<T>()
    data class Failure(val exception: Exception?,val message:String?,val code:Int?): XNetworkResponse<Nothing>()
}

fun <T> XNetworkResponse<T>.getValueFromResponse():T?{
    return when(this){
        is XNetworkResponse.Success -> {
            WaitDialog.dismiss()
            this.value
        }
        is XNetworkResponse.Failure -> {
            WaitDialog.dismiss()
            MessageDialog.show("Unknown Error",this.exception?.message ?: this.message,"Ok")
            null
        }
    }
}
