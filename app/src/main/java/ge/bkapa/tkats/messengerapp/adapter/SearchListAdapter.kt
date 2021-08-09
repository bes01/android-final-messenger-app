package ge.bkapa.tkats.messengerapp.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.presenter.SearchAdapterPresenter
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.SearchActivity

class SearchListAdapter(private val users: List<User>, private val activity: SearchActivity) :
    RecyclerView.Adapter<SearchListAdapter.UserViewHolder>() {

    val presenter = SearchAdapterPresenter()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
        if (position == users.size - 1) {
            activity.getNextUserChunk()
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userImage: ImageView = itemView.findViewById(R.id.search_item_img)
        private val nickname: TextView = itemView.findViewById(R.id.nickname_text_view)
        private val whatIDo: TextView = itemView.findViewById(R.id.what_i_do_text_view)

        fun bind(item: User) {
            nickname.text = item.nickname
            whatIDo.text = item.whatIdo
            itemView.setOnClickListener {
                activity.startChatActivity(item)
            }
            presenter.makeDownloadImageRequest(item.username, this::resolveImage)
        }

        private fun resolveImage(bitmap: Bitmap?) {
            itemView.findViewById<ProgressBar>(R.id.item_image_loading).visibility = View.GONE
            if (bitmap != null) {
                userImage.setImageBitmap(bitmap)
            } else {
                userImage.setImageResource(R.drawable.avatar_image_placeholder)
            }
        }

    }
}