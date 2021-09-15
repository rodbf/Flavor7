package com.flavorplus.components.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.flavorplus.R;
import com.flavorplus.util.Server;
import com.flavorplus.util.sqlite.DBManager;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchAutoCompleteAdapter extends ArrayAdapter<SearchAutoCompleteItem> {

    private Context context;

    public SearchAutoCompleteAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_auto_complete_item, parent, false);

        TextView tvSuggestion = convertView.findViewById(R.id.tv_search_autocomplete);
        ImageView ivSuggestion = convertView.findViewById(R.id.iv_ic_autocomplete);

        SearchAutoCompleteItem searchItem = getItem(position);

        tvSuggestion.setText(searchItem.getStr());
        ivSuggestion.setImageResource(searchItem.getIcon());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<SearchAutoCompleteItem> suggestions = new ArrayList<>();

                if(constraint != null){

                    DBManager dbManager = new DBManager(context);
                    dbManager.open();
                    Cursor history = dbManager.fetchLike(constraint.toString());
                    dbManager.close();
                    history.moveToFirst();
                    while(!history.isAfterLast()){
                        suggestions.add(new SearchAutoCompleteItem(history.getString(0), R.drawable.ic_history));
                        history.moveToNext();
                    }

                    String url = Server.APP_SERVER + "/pubrecipes/name/" + constraint.toString().toLowerCase().trim();

                    RequestFuture<JSONArray> future = RequestFuture.newFuture();
                    JsonArrayRequest request = new JsonArrayRequest(url, future, future);
                    VolleySingleton.getInstance().addToRequestQueue(request, "autocomplete");

                    JSONArray response = new JSONArray();
                    try{
                        response = future.get(5L, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                    }

                    for(int i = 0; i < response.length() && suggestions.size() < 5; i++){
                        try {
                            suggestions.add(new SearchAutoCompleteItem(((JSONObject)response.get(i)).getString("name"),R.drawable.ic_magnifier ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();

                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                if(results.values != null){
                    addAll((List) results.values);
                }
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((SearchAutoCompleteItem) resultValue).getStr();
            }
        };
        return searchFilter;
    }
}
