package com.nex3z.examples.bluetoothdiscovery;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BtDeviceAdapter extends RecyclerView.Adapter<BtDeviceAdapter.ViewHolder> {

    private List<BluetoothDevice> mDevices;
    private OnBtDeviceClickListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_bt_device, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);

        if (device.getName() == null) {
            holder.mTvName.setText(R.string.unknown);
        } else {
            holder.mTvName.setText(device.getName());
        }

        holder.mTvAddress.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    public void setDevices(List<BluetoothDevice> devices) {
        mDevices = devices;
        notifyDataSetChanged();
    }

    public void setOnBtDeviceClickListener(OnBtDeviceClickListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private TextView mTvAddress;

        ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onBtDeviceClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnBtDeviceClickListener {
        void onBtDeviceClick(int position);
    }
}
