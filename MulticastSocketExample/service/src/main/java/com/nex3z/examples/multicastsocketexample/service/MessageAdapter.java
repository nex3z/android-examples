package com.nex3z.examples.multicastsocketexample.service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageModel> mMessages;
    private int mLocalColor;
    private String mLocalAddr;

    public MessageAdapter(Context context) {
        mLocalColor = ContextCompat.getColor(context, R.color.colorLocal);
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mLocalAddr = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageModel message = mMessages.get(position);

        holder.mTvAddress.setText(message.getAddress());
        holder.mTvPort.setText(String.valueOf(message.getPort()));
        holder.mTvMessage.setText(message.getMessage());

        if (message.getAddress().equals(mLocalAddr)) {
            holder.mTvAddress.setTextColor(mLocalColor);
            holder.mTvPort.setTextColor(mLocalColor);
            holder.mTvMessage.setTextColor(mLocalColor);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    public void setMessageCollection(Collection<MessageModel> collection) {
        validateCollection(collection);
        mMessages = (List<MessageModel>) collection;
        notifyDataSetChanged();
    }

    private void validateCollection(Collection<MessageModel> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAddress;
        TextView mTvPort;
        TextView mTvMessage;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            mTvPort = (TextView) itemView.findViewById(R.id.tv_port);
            mTvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
