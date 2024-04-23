import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.data.Section
import com.xdeveloperss.fourbrothers.databinding.ShopItemBinding

class SectionHeaderAdapter<T>(
    private val sectionList: List<Section<T>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SECTION_HEADER_TYPE = 0
        private const val ITEM_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == SECTION_HEADER_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
            SectionHeaderViewHolder(view)
        } else {
            GenericViewHolder(ShopItemBinding.inflate(inflater, parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SectionHeaderViewHolder) {
            holder.bind(sectionList[getSectionIndex(position)].name)
        } else {
//            val item = getItem(position)
//            (holder as SectionHeaderAdapter<*>.GenericViewHolder).bind(item, position)
        }
    }

    override fun getItemCount(): Int {
        return sectionList.sumBy { it.items.size + 1 }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSectionHeader(position)) {
            SECTION_HEADER_TYPE
        } else {
            ITEM_TYPE
        }
    }

    private fun isSectionHeader(position: Int): Boolean {
        return position == 0 || sectionList.fold(0) { acc, section ->
            acc + section.items.size + 1
        }.let { it <= position }
    }

    private fun getSectionIndex(position: Int): Int {
        var count = 0
        var index = 0
        for (section in sectionList) {
            if (count == position) {
                return index
            }
            count += section.items.size + 1
            index++
        }
        return -1
    }

    private fun getItem(position: Int): T {
        var count = 0
        for (section in sectionList) {
            if (position == count) {
                throw IllegalStateException("Invalid position")
            }
            if (position < count + section.items.size + 1) {
                return section.items[position - count - 1]
            }
            count += section.items.size + 1
        }
        throw IllegalStateException("Invalid position")
    }

    // ViewHolder for section headers
    class SectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sectionHeaderTextView: TextView = view.findViewById(R.id.sectionTextView)

        fun bind(sectionHeader: String) {
            sectionHeaderTextView.text = sectionHeader
        }
    }
    inner class GenericViewHolder(var b: ViewBinding) : RecyclerView.ViewHolder(b.root)

    // Interface to bind generic model data
    interface Bindable<T> {
        fun bind(item: T, position: Int)
    }
}
