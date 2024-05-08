package com.xdeveloperss.fourbrothers.ui.main.ui.employee

import com.xdeveloperss.fourbrothers.databinding.FragmentEmployeeDetailBinding
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmployeeDetailFragment: XBaseFragment<FragmentEmployeeDetailBinding>(FragmentEmployeeDetailBinding::inflate)  {

    private val employeeViewModel: EmployeeViewModel by sharedViewModel()

    override fun onViewCreated() {
        employeeViewModel.employeeDetail.observe {
            barTitle(it.name, it.phonenumber)
        }
    }

}