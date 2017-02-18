package com.nex3z.examples.multicastsocketexample;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageModel> mMessages;
    private int mLocalColor;
    private int mRemoteColor;
    private int mReceiveColor;
    private int mSendColor;
    private String mLocalAddr;

    public MessageAdapter(Context context) {
        mLocalColor = ContextCompat.getColor(context, R.color.colorLocal);
        mRemoteColor = ContextCompat.getColor(context, R.color.colorRemote);
        mReceiveColor = ContextCompat.getColor(context, R.color.colorReceive);
        mSendColor = ContextCompat.getColor(context, R.color.colorSend);

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
        holder.mTvNumber.setText(String.valueOf(position));

        if (message.getAddress().equals(mLocalAddr)) {
            setTextColor(holder, mLocalColor);
        } else {
            setTextColor(holder, mRemoteColor);
        }

        if (message.getDirection() == MessageModel.RECV) {
            holder.mViewDirection.setBackgroundColor(mReceiveColor);
        } else {
            holder.mViewDirection.setBackgroundColor(mSendColor);
        }
    }

    private void setTextColor(ViewHolder holder, int color) {
        holder.mTvAddress.setTextColor(color);
        holder.mTvPort.setTextColor(color);
        holder.mTvMessage.setTextColor(color);
        holder.mTvNumber.setTextColor(color);
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
        TextView mTvNumber;
        View mViewDirection;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            mTvPort = (TextView) itemView.findViewById(R.id.tv_port);
            mTvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            mTvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            mViewDirection = itemView.findViewById(R.id.view_direction);
        }
    }
}
