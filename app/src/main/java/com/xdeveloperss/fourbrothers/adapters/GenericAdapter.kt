package com.xdeveloperss.fourbrothers.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.XBaseApplication
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.databinding.ShopItemBinding
import com.xdeveloperss.fourbrothers.utils.FileManager


enum class AdapterType{
    SHOP,
}
enum class AdapterAction(val id: Int, val image: Int){
    EDIT(R.string.edit, R.drawable.baseline_edit_24),
    DELETE(R.string.delete, R.drawable.baseline_delete_outline_24),
    SELECT(R.string.select, R.drawable.baseline_pan_tool_alt_24),

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
            AdapterType.SHOP -> {
                GenericViewHolder(ShopItemBinding.inflate(inflater, parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        when(type){
            AdapterType.SHOP ->{
                val data = lists[position] as ShopItemData
                val b =  holder.b as ShopItemBinding
                b.personName.text = data.personName
                b.weight.text = data.weight.toString() +" Kg"
                b.amount.text = data.total.toInt().toString() +" Rs"

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