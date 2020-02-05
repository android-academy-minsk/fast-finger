package by.android.academy.minsk.fastfinger.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.android.academy.minsk.fastfinger.R
import by.android.academy.minsk.fastfinger.android.AndroidResourceManager
import kotlinx.android.synthetic.main.chat_fragment.*

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.chat_fragment, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatViewModel = ViewModelProviders.of(
            this, chatViewModelFactory(AndroidResourceManager(resources))
        ).get(ChatViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context!!)
        messages.layoutManager = linearLayoutManager
        val adapter = MessagesAdapter()
        messages.adapter = adapter
        chatViewModel.chatText.observe(viewLifecycleOwner) {
            val needToScroll =
                linearLayoutManager.findLastVisibleItemPosition() > adapter.messages.size - 3
            adapter.messages = it
            if (needToScroll) {
                messages.smoothScrollToPosition(it.size)
            }
        }
        sendMessageButton.setOnClickListener {
            val text = newMessageText.text.toString()
            newMessageText.text?.clear()
            chatViewModel.sendMessage(text)
        }
    }

    private class MessageViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {
        fun bind(value: String) {
            view.text = value
        }
    }

    private inner class MessagesAdapter : RecyclerView.Adapter<MessageViewHolder>() {

        private var _messages = emptyList<String>()
        var messages
            get() = _messages
            set(value) {
                _messages = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.message_view, parent, false)
            return MessageViewHolder(view as TextView)
        }

        override fun getItemCount() = messages.size

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.bind(messages[position])
        }
    }
}

