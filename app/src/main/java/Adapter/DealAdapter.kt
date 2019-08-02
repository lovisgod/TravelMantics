package Adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lovisgod.travelmantics.InsertActivity
import com.lovisgod.travelmantics.R
import com.lovisgod.travelmantics.TravelDeal

class DealAdapter: RecyclerView.Adapter<DealAdapter.DealViewHolder>() {
     private var deals: ArrayList<TravelDeal> = ArrayList<TravelDeal>()

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_deals, parent, false)
        return DealViewHolder(itemView)
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        var deal: TravelDeal = deals.get(position)
        holder.bind(deal)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        if (deals !=null){
            return  deals.size
        }
        return 0
    }

    fun setDealSList (dealslist : ArrayList<TravelDeal>) {
        this.deals = dealslist
        notifyItemInserted(dealslist.size-1)
    }

    inner class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        var tvPrice = itemView.findViewById<TextView>(R.id.rv_price)
        var tvDescription = itemView.findViewById<TextView>(R.id.tv_description)
        var tvimage = itemView.findViewById<ImageView>(R.id.dealimage)

        init {
            itemView.setOnClickListener(this)

        }
        fun bind(deal:TravelDeal) {
            tvTitle.text = deal.title
            tvPrice.text = deal.price
            tvDescription.text = deal.description
            tvimage.setImageURI(Uri.parse(deal.imageUrl))
        }

        override fun onClick(p0: View?) {
            var position = adapterPosition
            Log.d("TAG", "This is the position -> $position")
            var selectedDeal = deals.get(position)
            Log.d("TAG", "This is the id -> ${selectedDeal.id}")
            val intent = Intent(p0!!.context, InsertActivity::class.java)
            intent.putExtra("Deal", selectedDeal)
            p0.context.startActivity(intent)
        }
    }
}