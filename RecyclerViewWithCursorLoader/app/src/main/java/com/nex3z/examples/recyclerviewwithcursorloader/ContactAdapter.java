package com.nex3z.examples.recyclerviewwithcursorloader;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactAdapter extends  RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Cursor mCursor;

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(MainActivityFragment.COL_DISPLAY_NAME);
        holder.mContact.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mContact;

        public ViewHolder(View itemView) {
            super(itemView);
            mContact = (TextView) itemView.findViewById(R.id.contact);
        }
    }
}
