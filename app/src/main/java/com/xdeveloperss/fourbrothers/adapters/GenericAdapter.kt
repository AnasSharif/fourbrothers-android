package com.xdeveloperss.fourbrothers.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.StringUtils.getString
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.XBaseApplication
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.models.VendorSupplie
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieExpense
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieItems
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.databinding.ExpenseItemBinding
import com.xdeveloperss.fourbrothers.databinding.KachraPaymentItemBinding
import com.xdeveloperss.fourbrothers.databinding.ProductItemBinding
import com.xdeveloperss.fourbrothers.databinding.ShopItemBinding
import com.xdeveloperss.fourbrothers.databinding.SupplieExpenseItemBinding
import com.xdeveloperss.fourbrothers.databinding.SupplieItemBinding
import com.xdeveloperss.fourbrothers.databinding.SupplyPartyItemBinding
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.glideLoad
import com.xdeveloperss.fourbrothers.utils.hide
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs


enum class AdapterType{
    SHOP,
    PERSON,
    SUPPLY,
    SUPPLY_PARTY,
    SUPPLY_EXPENSE,
    EXPENSE,
    PRODUCT,
    KACHRA_PAYMENT
}
enum class AdapterAction(val id: Int, val image: Int){
    EDIT(R.string.edit, R.drawable.baseline_edit_24),
    DELETE(R.string.delete, R.drawable.baseline_delete_outline_24),
    SELECT(R.string.select, R.drawable.baseline_pan_tool_alt_24),
    PICKER(R.string.select, R.drawable.baseline_pan_tool_alt_24),

}
class GenericAdapter<T>(val type: AdapterType = AdapterType.SHOP, private val lists: List<T>,
                        var actions:(Int, AdapterAction, T)->Unit) :
    RecyclerView.Adapter<GenericAdapter<T>.GenericViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(type){
            AdapterType.SHOP -> { GenericViewHolder(ShopItemBinding.inflate(inflater, parent,false)) }
            AdapterType.PERSON -> { GenericViewHolder(ShopItemBinding.inflate(inflater, parent,false))}
            AdapterType.SUPPLY -> { GenericViewHolder(SupplieItemBinding.inflate(inflater, parent,false))}
            AdapterType.SUPPLY_PARTY -> { GenericViewHolder(SupplyPartyItemBinding.inflate(inflater, parent,false)) }
            AdapterType.SUPPLY_EXPENSE ->  { GenericViewHolder(SupplieExpenseItemBinding.inflate(inflater, parent,false)) }
            AdapterType.EXPENSE -> { GenericViewHolder(ExpenseItemBinding.inflate(inflater, parent,false)) }
            AdapterType.PRODUCT -> { GenericViewHolder(ProductItemBinding.inflate(inflater, parent,false)) }
            AdapterType.KACHRA_PAYMENT ->  { GenericViewHolder(KachraPaymentItemBinding.inflate(inflater, parent,false)) }
        }
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        when(type){
            AdapterType.SHOP ->{
                val data = lists[position] as OrderItems
                val b =  holder.b as ShopItemBinding
                b.personName.text = data.personName
                b.weight.text = data.weight.toString() +" Kg"
                b.amount.text = data.total.toInt().toString() +" Rs"

            }
            AdapterType.PERSON -> {
                val data = lists[position] as Person
                val b =  holder.b as ShopItemBinding
                b.personName.text = data.name
                b.weight.text = data.phonenumber
            }

            AdapterType.SUPPLY -> {
                val data = lists[position] as Supply
                val b =  holder.b as SupplieItemBinding
                b.supplierName.text = data.supplier.name
                b.supplierRate.text = getString(R.string.rate, data.rate)
                b.supplierWeight.text =  getString(R.string.total_weight, data.weight)
                data.vendorSupplie.media?.let {
                    val adapter = PagerAdapter(XBaseApplication.xCon(), it.toMutableList())
                    b.viewPagerMain.adapter = adapter
                }
                val supplierTotal = data.rate*data.weight
                val suppliesTotal = data.vendorSupplie.items.sumOf { it.total }
                val suppliesExpenses = data.vendorSupplie.expenses.sumOf { it.amount }
                b.supplyContainer.hide(!Prefs.isAdmin())
                b.supplierAmount.text = supplierTotal.toInt().toString()
                b.suppliesAmount.text = suppliesTotal.toString()
                b.totalExpense.text = suppliesExpenses.toInt().toString()
                b.totalBachat.text = getString(R.string.total, (suppliesTotal-supplierTotal-suppliesExpenses).toInt())
                b.imagePicker.setOnClickListener {
                    actions(position, AdapterAction.PICKER, lists[position])
                }
            }

            AdapterType.SUPPLY_PARTY -> {
                val data = lists[position] as VendorSupplieItems
                val b =  holder.b as SupplyPartyItemBinding
                b.supplierName.text = data.vendor.name
                b.supplierRate.text = getString(R.string.rate, data.rate)
                b.supplierWeight.text =  getString(R.string.total_weight, data.weight)
                data.media?.let {
                    val adapter = PagerAdapter(XBaseApplication.xCon(), it.toMutableList())
                    b.viewPagerMain.adapter = adapter
                }
                b.imagePicker.setOnClickListener {
                    actions(position, AdapterAction.PICKER, lists[position])
                }
            }

            AdapterType.SUPPLY_EXPENSE -> {
                val data = lists[position] as VendorSupplieExpense
                val b =  holder.b as SupplieExpenseItemBinding
                b.expenseName.text = data.type.name
                b.expenseAmount.text = getString(R.string.total, data.amount.toLong())
            }

            AdapterType.EXPENSE -> {
                val data = lists[position] as Expense
                val b =  holder.b as ExpenseItemBinding
                b.expenseName.text = data.type?.name.toString()
                b.expenseAmount.text = getString(R.string.total, data.amount?.toLong())
                b.expenseDesc.text = data.desc
                data.media.let {
                    val adapter = PagerAdapter(XBaseApplication.xCon(), it.toMutableList())
                    b.viewPagerMain.adapter = adapter
                }
                b.imagePicker.setOnClickListener {
                    actions(position, AdapterAction.PICKER, lists[position])
                }
            }
            AdapterType.PRODUCT -> {
                val data = lists[position] as Product
                val b =  holder.b as ProductItemBinding
                b.productName.text = data.name
                if (data.media.isNotEmpty()){
                    loadImage(data.media.first().file_name.toString(), b.productImage)
                }else{
                    b.productImage.setImageBitmap(null)
                }
                b.imagePicker.setOnClickListener {
                    actions(position, AdapterAction.PICKER, lists[position])
                }
            }

            AdapterType.KACHRA_PAYMENT ->{
                val data = lists[position] as KachraPayment
                val b =  holder.b as KachraPaymentItemBinding
                b.kachraAmount.text = getString(R.string.total, data.amount.value().toLong())
                b.kachraWeight.text = getString(R.string.total_weight, data.weight)
                b.typeText.text = if (data.paymentType == "cash") "C" else "A"
                b.productName.text = data.product?.name ?: "---"
                b.shopName.text = data.person?.name ?: "---"
                b.imagePicker.setOnClickListener {
                    actions(position, AdapterAction.PICKER, lists[position])
                }
                data.media.let {
                    val adapter = PagerAdapter(XBaseApplication.xCon(), it.toMutableList())
                    b.viewPagerMain.adapter = adapter
                }
            }
        }
        holder.b.root.setOnClickListener {
            actions(position, AdapterAction.SELECT, lists[position])
        }
    }
    private fun moreOptions(position: Int, array: List<AdapterAction> = listOf()){
        val context = XBaseApplication.xCon()
        val menuBtn = listOf(AdapterAction.EDIT, AdapterAction.DELETE).plus(array)
        val stringArray = mutableListOf<String>()
         for (menu in menuBtn){
             stringArray.add(context.getString(menu.id))
         }
        PopMenu.show(stringArray.toTypedArray())
            .setOnMenuItemClickListener { _, _, index ->
                when (index) {
                    0 ->  actions(position, AdapterAction.EDIT, lists[position])
                    1 -> MessageDialog.show(context.getString(R.string.warning),
                        context.getString(R.string.are_you_sure_to_delete_this_item),
                        context.getString(
                            R.string.yes_sure
                        ))
                        .setOkButton { _, _ ->
                            actions(position, AdapterAction.DELETE, lists[position])
                            false
                        }
                }
                false
            }.onIconChangeCallBack = object : OnIconChangeCallBack<PopMenu?>(true) {
            override fun getIcon(dialog: PopMenu?, index: Int, menuText: String): Int {
                return menuBtn[index].image
            }
        }
    }

    private fun loadImage(name: String, imageView: ImageView){
        Glide.with(XBaseApplication.xCon())
            .load(FileManager.getFileForGlide(name))
            .placeholder(R.drawable.baseline_image_24)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return lists.size
    }
    inner class GenericViewHolder(var b: ViewBinding) : RecyclerView.ViewHolder(b.root)
}