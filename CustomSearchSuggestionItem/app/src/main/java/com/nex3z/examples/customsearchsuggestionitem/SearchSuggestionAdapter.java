package com.nex3z.examples.customsearchsuggestionitem;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchSuggestionAdapter extends CursorAdapter {
    private static final String LOG_TAG = SearchSuggestionAdapter.class.getSimpleName();

    public SearchSuggestionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.search_suggestion_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.mTitle.setText(
                cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
    }

    public String getSuggestionText(int position) {
        if (position >= 0 && position < getCursor().getCount()) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        }
        return null;
    }

    public static class ViewHolder {
        public ImageView mIcon;
        public TextView mTitle;

        public ViewHolder(View view) {
            mIcon = (ImageView) view.findViewById(R.id.iv_suggestion_item_icon);
            mTitle = (TextView) view.findViewById(R.id.tv_suggestion_item_title);
        }
    }
}
