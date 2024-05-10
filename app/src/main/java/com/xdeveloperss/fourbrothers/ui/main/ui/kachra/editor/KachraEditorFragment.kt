package com.xdeveloperss.fourbrothers.ui.main.ui.kachra.editor

import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.PersonRate
import com.xdeveloperss.fourbrothers.databinding.FragmentKachraEditorBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.kachra.KachraViewModel
import com.xdeveloperss.fourbrothers.utils.backWithDelay
import com.xdeveloperss.fourbrothers.utils.double
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.utils.textOrEmpty
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class KachraEditorFragment : XBaseFragment<FragmentKachraEditorBinding>(FragmentKachraEditorBinding::inflate) {

    private val kachraViewModel: KachraViewModel by sharedViewModel()

    private var selectRate: PersonRate? = null

    private var payment: KachraPayment? = null
    override fun onViewCreated() {

        binding.root.backWithDelay {
            kachraViewModel.setData(null, listOf("shop"))
        }
        kachraViewModel.selectPayment.observe {
            payment = it
            binding.textFieldProducts.textOrEmpty(payment?.personProduct?.product?.name)
            binding.textFieldRateType.textOrEmpty(payment?.personProduct?.rate?.type?.name)
            binding.textFieldRates.text(payment?.personProduct?.rate?.amount)
            binding.textFieldSelectParty.textOrEmpty(payment?.person?.name)
            binding.fieldWeight.text(payment?.weight)
            binding.fieldAmount.text(payment?.amount)
            this.selectRate = it.personProduct?.rate
        }
        kachraViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.shop?.let {
                this.loadPersonProduct(it.map { it.name }.toList(), binding.textFieldSelectParty)
                { s, i ->
                    payment?.shopID = it[i].id
                }
            }
        }
        kachraViewModel.personProducts.observe{
            this.loadPersonProduct(it.map { it.product.name.toString() }, binding.textFieldProducts)
            { s, j ->
                payment?.personProductsID = it[j].id
                selectRate = it[j].rate
                binding.textFieldRateType.text(selectRate?.type?.name)
                binding.textFieldRates.text(selectRate?.amount?.toInt())
            }
        }
        binding.fieldWeight.editText?.addTextChangedListener { text ->
            selectRate?.let {
                binding.fieldAmount.text(it.amount*text.double)
                payment?.weight = text.double
                payment?.amount = it.amount*text.double
            }
        }
        binding.saveBtn.setOnClickListener {
            if (payment?.personProductsID == null){
                binding.textFieldProducts.error = "Kachra Product is required"
                return@setOnClickListener
            }
            binding.textFieldProducts.error = null
            if (payment?.shopID == null){
                binding.textFieldSelectParty.error = "Kachra Shop is required"
                return@setOnClickListener
            }
            binding.textFieldSelectParty.error = null
            if (payment?.weight.value() == 0.0){
                binding.fieldWeight.error = "Enter kachra weight"
                return@setOnClickListener
            }
            binding.textFieldProducts.error = null
            kachraViewModel.saveData(null, typeString = "dailyKacharaPayment", payment)
            findNavController().popBackStack()
        }

        binding.fieldWeight.editText?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.saveBtn.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

}