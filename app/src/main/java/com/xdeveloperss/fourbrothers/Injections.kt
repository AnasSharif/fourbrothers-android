package com.xdeveloperss.fourbrothers

import com.xdeveloperss.fourbrothers.ui.join.data.AuthRepoImpl
import com.xdeveloperss.fourbrothers.ui.join.data.AuthRepository
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainRepoImpl
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.expense.ExpenseViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.parties.PartyViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.product.ProductViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieViewModel
import com.xdeveloperss.fourbrothers.xnetwork.config.configureApi
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repos = module {
    single<AuthRepository> {
        AuthRepoImpl(get())
    }
    single<MainRepo> {
        MainRepoImpl(get())
    }
}
val vms = module {
    viewModel {
        AuthViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        ShopViewModel(get())
    }
    viewModel {
        ExpenseViewModel(get())
    }
    viewModel {
        ProductViewModel(get())
    }
    viewModel {
        SupplieViewModel(get())
    }
    viewModel {
        PartyViewModel(get())
    }
}
val apiModule = module {
    single {
        configureApi(ServerInterface::class.java, BuildConfig.API_URL)
    }
}