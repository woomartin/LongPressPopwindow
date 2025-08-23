package com.example.app.longpresspopwindow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages;
    private OnMessageActionListener actionListener;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void setOnMessageActionListener(OnMessageActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).bind(message, position);
        } else {
            ((ReceivedMessageHolder) holder).bind(message, position);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void removeMessage(int position) {
        if (position >= 0 && position < messages.size()) {
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTime;

        SentMessageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }

        void bind(Message message, int position) {
            textMessage.setText(message.getContent());
            textTime.setText(message.getTime());
            
            // 设置长按监听器
            textMessage.setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_message_options, null);
                PopupMenuUtil.showPopupMenu(v, popupView);
                return true;
            });
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTime;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }

        void bind(Message message, int position) {
            textMessage.setText(message.getContent());
            textTime.setText(message.getTime());
            
            // 设置长按监听器
            textMessage.setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_message_options, null);
                PopupMenuUtil.showPopupMenu(v, popupView);
                return true;
            });
        }
    }

    public interface OnMessageActionListener {
        void onDeleteMessage(int position);
        void onReplyMessage(Message message);
    }
}
