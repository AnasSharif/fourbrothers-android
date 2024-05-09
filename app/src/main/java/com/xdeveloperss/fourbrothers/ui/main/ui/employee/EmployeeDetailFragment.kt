package com.xdeveloperss.fourbrothers.ui.main.ui.employee

import android.graphics.Bitmap
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.databinding.FragmentEmployeeDetailBinding
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmployeeDetailFragment: XBaseFragment<FragmentEmployeeDetailBinding>(FragmentEmployeeDetailBinding::inflate)  {

    private val employeeViewModel: EmployeeViewModel by sharedViewModel()

    override fun onViewCreated() {
        employeeViewModel.employeeDetail.observe {
            barTitle(it.name, it.phonenumber)
            binding.employeeType.text =  getString(R.string.type, it.type?.name ?: "---")
            binding.currentSalary.text = getString(R.string.current_salary, it.currentSalary?.salary ?: 0)

            binding.paymentsRV.adapter = GenericAdapter(type = AdapterType.EMPLOYEE, it.payments ?: listOf())
            { i, action, pro ->
                when(action){
                    AdapterAction.EDIT -> {}
                    AdapterAction.DELETE -> {}
                    AdapterAction.SELECT -> {

                    }
                    AdapterAction.PICKER -> {
                        cropImage.launch(
                            options{
                                setGuidelines(CropImageView.Guidelines.ON)
                                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                            }
                        )
                    }
                }
            }
        }
    }

}