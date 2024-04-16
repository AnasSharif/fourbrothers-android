package com.xdeveloperss.fourbrothers

import com.xdeveloperss.fourbrothers.ui.join.data.AuthRepoImpl
import com.xdeveloperss.fourbrothers.ui.join.data.AuthRepository
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.xnetwork.config.configureApi
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repos = module {
    single<AuthRepository> {
        AuthRepoImpl(get())
    }
}
val vms = module {
    viewModel {
        AuthViewModel(get())
    }
}
val apiModule = module {
    single {
        configureApi(ServerInterface::class.java, BuildConfig.API_URL)
    }
}