package ge.bkapa.tkats.messengerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.bkapa.tkats.messengerapp.R
import ge.bkapa.tkats.messengerapp.storage.model.User
import ge.bkapa.tkats.messengerapp.view.SearchActivity

class SearchListAdapter(private val users: List<User>, private val activity: SearchActivity) :
    RecyclerView.Adapter<SearchListAdapter.UserViewHolder>() {

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
        // TODO: fetch User Image[bkapa]
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: User) {
            userImage.setImageResource(R.drawable.avatar_image_placeholder)
            nickname.text = item.nickname
            whatIDo.text = item.whatIdo
            itemView.setOnClickListener {
                activity.startChatActivity(item)
            }
        }

        val userImage: ImageView = itemView.findViewById(R.id.search_item_img)
        val nickname: TextView = itemView.findViewById(R.id.nickname_text_view)
        val whatIDo: TextView = itemView.findViewById(R.id.what_i_do_text_view)

    }
}