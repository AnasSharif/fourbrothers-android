package com.xdeveloperss.fourbrothers.ui.join

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.BuildConfig
import com.xdeveloperss.fourbrothers.MainActivity
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.databinding.ActivityLoginBinding
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.xbase.XBaseActivity
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : XBaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private val authViewModel: AuthViewModel by viewModel()
    private var email: String? = null
    private var pass: String? = null
    override fun initViews() {

        if (BuildConfig.DEBUG) {
            binding.editTextTextEmailAddress.editText?.setText("waqaskhan")
            binding.editTextTextPassword.editText?.setText("waqas#09091")
        }

        binding.joinButton.setOnClickListener {
            email = binding.editTextTextEmailAddress.editText?.text.toString()
            pass = binding.editTextTextPassword.editText?.text.toString()

            authViewModel.login(email.toString(), pass.toString())
            WaitDialog.show("Login...")
        }
        authViewModel.apiLogin.observe(this){
            it.getValueFromResponse()?.let {
                if (it.success){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    MessageDialog.show("Error", it.message, "Ok")
                }
            }
        }
    }
}