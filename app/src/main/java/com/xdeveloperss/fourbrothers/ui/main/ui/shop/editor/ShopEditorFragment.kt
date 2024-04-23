package com.xdeveloperss.fourbrothers.ui.main.ui.shop.editor

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.data.responses.ShopResponse
import com.xdeveloperss.fourbrothers.databinding.FragmentShopBinding
import com.xdeveloperss.fourbrothers.databinding.FragmentShopEditorBinding
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class ShopEditorFragment : XBaseFragment<FragmentShopEditorBinding>(FragmentShopEditorBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private lateinit var shopData: ShopItemData
    override fun onViewCreated() {
        shopViewModel.selectedData.observe {
            binding.textFieldSelectParty.editText?.setText(it[1]?.personName)
            binding.fieldAmount.editText?.setText(it[1]?.total.toString())
            binding.fieldWeight.editText?.setText(it[1]?.weight.toString())
        }

        binding.imageCard.setOnClickListener {
            cropImage.launch(
                options{
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                }
            )
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val imageUri = result.uriContent
            Glide.with(requireActivity()).load(imageUri).into(binding.itemImage)
            AppExecutors().getInstance()?.diskIO()?.execute {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                binding.itemImage.tag = FileManager.createFileFromPath(bitmap=bitmap)
            }
        }
    }
}