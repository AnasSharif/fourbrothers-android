package com.xdeveloperss.fourbrothers.ui.main.ui.employee

import android.graphics.Bitmap
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.databinding.FragmentEmployeeBinding
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmployeeFragment: XBaseFragment<FragmentEmployeeBinding>(FragmentEmployeeBinding::inflate)  {

    private val employeeViewModel: EmployeeViewModel by sharedViewModel()

    override fun onViewCreated() {

        barTitle(getString(R.string.employee), null)
        employeeViewModel.setData(null, listOf("employees"))
        employeeViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                val pp =  it.employees.sortedByDescending { it.createdAt }
                binding.employeeRV.adapter = GenericAdapter(type = AdapterType.PERSON, pp)
                { i, action, pro ->
                    when(action){
                        AdapterAction.EDIT -> {}
                        AdapterAction.DELETE -> {}
                        AdapterAction.SELECT -> {
                            findNavController().navigate(EmployeeFragmentDirections.actionNavEmployeeToEmployeeDetailFragment())
                            employeeViewModel.setEmployee(pro)
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

}