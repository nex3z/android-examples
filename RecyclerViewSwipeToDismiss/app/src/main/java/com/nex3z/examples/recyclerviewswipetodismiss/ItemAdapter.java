package com.nex3z.examples.recyclerviewswipetodismiss;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<String> mTextList;

    public ItemAdapter(List<String> textList) {
        mTextList = textList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = mTextList.get(position);
        holder.mText.setText(text);
    }

    @Override
    public int getItemCount() {
        return mTextList != null ? mTextList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;

        public ViewHolder(View view) {
            super(view);
            mText = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
