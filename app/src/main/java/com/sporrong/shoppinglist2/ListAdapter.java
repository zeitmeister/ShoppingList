package com.sporrong.shoppinglist2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ShoppingItemViewHolder> {

    private static final String ITEMS_FIREBASE_KEY = "ItemsList";
    private ArrayList<ShoppingItem> mShoppingItems = new ArrayList<>();

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View listItemView = inflater.inflate(R.layout.list_item, parent, false);
        return new ShoppingItemViewHolder(listItemView);
    }

   // @Override
  public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {
        ShoppingItem shoppingItem = mShoppingItems.get(position);
        holder.bind(shoppingItem);
    }

    @Override
    public int getItemCount() {
        return mShoppingItems.size();
    }

    public void addItem(ShoppingItem shoppingItem){
        mShoppingItems.add(shoppingItem);
        notifyDataSetChanged();
    }

    public void removeItem(ShoppingItem shoppingItem){
        mShoppingItems.remove(shoppingItem);
        notifyDataSetChanged();
    }

    public void clearItemList() {
        mShoppingItems.clear();
        notifyDataSetChanged();
    }

    public boolean updateBought(ShoppingItem shoppingItem) {
        return !shoppingItem.bought;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}


