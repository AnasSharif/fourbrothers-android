package com.xdeveloperss.fourbrothers.custom

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.responses.DailyRates
import com.xdeveloperss.fourbrothers.databinding.RateDialogViewBinding
import com.xdeveloperss.fourbrothers.databinding.SupplierDialogBinding
import com.xdeveloperss.fourbrothers.utils.Int
import com.xdeveloperss.fourbrothers.utils.double
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.utils.value

enum class XDialogType{
    DAILY_RATES,
    SUPPLER
}
class XDialogBuilder<T>(private var context: FragmentActivity, private val modelClass: T) {

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var binding: ViewBinding
    private var dialogType: XDialogType = XDialogType.DAILY_RATES
    private var completion: ((Any) -> Unit)? = null
    private fun viewBinding():XDialogBuilder<T>{
        this.materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
        binding = when(dialogType){
            XDialogType.DAILY_RATES -> {
                RateDialogViewBinding.inflate(LayoutInflater.from(context), null, false)
            }
            XDialogType.SUPPLER ->{
                SupplierDialogBinding.inflate(LayoutInflater.from(context), null, false)
            }
        }
        materialAlertDialogBuilder.setView(binding.root)
        return this
    }
    fun setData(type: XDialogType = XDialogType.DAILY_RATES, completion: (Any?) -> Unit): XDialogBuilder<T> {
        dialogType = type
        this.completion = completion
        viewBinding()
        modelClass.let {
            if (it is DailyRates){
               val b = binding as RateDialogViewBinding
                b.zindaRate.text(it.zindarate)
                b.chickenRate.text(it.chickenrate)
            }
            if (it is Supply){
                val b = binding as SupplierDialogBinding
                b.supplierRate.text(it.rate)
                b.supplyWeight.text(it.weight.toString())
                b.textView.text = it.supplier.name
            }
        }
        return this
    }

    fun show(positiveTitle: String = "Ok", negativeTitle: String = "Cancel",){
        materialAlertDialogBuilder
            .setPositiveButton(positiveTitle) { dialog, _ ->
                when(dialogType){
                    XDialogType.DAILY_RATES -> {
                        this.completion?.let { comp->
                            modelClass.let {
                                if (it is DailyRates){
                                    val b = binding as RateDialogViewBinding
                                    it.chickenrate = b.chickenRate.Int()
                                    it.zindarate = b.zindaRate.Int()
                                    comp(it)
                                }
                            }
                        }
                    }

                    XDialogType.SUPPLER -> {
                        this.completion?.let { comp->
                            modelClass.let {
                                if (it is Supply){
                                    val b = binding as SupplierDialogBinding
                                    it.rate = b.supplierRate.Int().value()
                                    it.weight = b.supplyWeight.double()
                                    it.weight = b.supplyWeight.double()
                                    comp(it)
                                }
                            }
                        }
                    }
                }
            }
            .setNegativeButton(negativeTitle) { dialog, _ ->

            dialog.dismiss()
        }
            .show()
    }
}