package com.jaroid.android51_demofirebase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ChatMessageModel> chatMessageModels;
    private final String selfEmail;
    private Context mContext;
    private final int SELF = 0;
    private final int FRIEND = 1;

    public HistoryAdapter(ArrayList<ChatMessageModel> chatMessageModels, String selfEmail) {
//        this.chatMessageModels = chatMessageModels;
        this.chatMessageModels = new ArrayList<>();
        this.chatMessageModels.addAll(chatMessageModels);
        this.selfEmail = selfEmail;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SELF) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_self, parent, false);
            return new MessageSelfViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_message_friend, parent, false);
            return new MessageFriendViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageModel chatMessageModel = chatMessageModels.get(position);
        if (holder instanceof MessageSelfViewHolder) {
            ((MessageSelfViewHolder) holder).tvMessage.setText(chatMessageModel.getChatMessage().getMessage());
        } else {
            ((MessageFriendViewHolder) holder).tvMessage.setText(chatMessageModel.getChatMessage().getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageModels.get(position).getName().equals(selfEmail)) {
            return SELF;
        } else {
            return FRIEND;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageModels.size();
    }

    public void updateData(ArrayList<ChatMessageModel> mListMessageHistory) {
        Log.d("TAG", "updateData: ");
        chatMessageModels.clear();
        this.chatMessageModels.addAll(mListMessageHistory);
        notifyDataSetChanged();
    }

    public void addData(ChatMessageModel chatMessageModel) {
        this.chatMessageModels.add(chatMessageModel);
        notifyItemInserted(getItemCount() - 1);
    }

    public class MessageSelfViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMessage;
        TextView tvMessage;

        public MessageSelfViewHolder(@NonNull View itemView) {
            super(itemView);
            llMessage = itemView.findViewById(R.id.llMessage);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    public class MessageFriendViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMessage;
        TextView tvMessage;

        public MessageFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            llMessage = itemView.findViewById(R.id.llMessage);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
