package com.xdeveloperss.fourbrothers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xdeveloperss.fourbrothers.ui.join.LoginActivity
import com.xdeveloperss.fourbrothers.ui.main.MainActivity
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isLogin()){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun isLogin():Boolean{
        Prefs.getToken()?.let {
            return true
        }
        return false
    }
}