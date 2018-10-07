package com.sporrong.shoppinglist2;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;

public class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView productName;
    private final Switch boughtSwitch;
    private final Button removeButton;
    private final EditText priceTag;
    private int priceInt;
    private TotalSum totalSumObj = new TotalSum();
    public static final String ITEMS_FIREBASE_KEY = "ItemsList";
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ITEMS_FIREBASE_KEY);


    public ShoppingItemViewHolder(View view) {
    super(view);
        productName = view.findViewById(R.id.itemTextView);
        boughtSwitch = view.findViewById(R.id.boughtSwitchId);
        removeButton = view.findViewById(R.id.removeButton);
        priceTag = view.findViewById(R.id.priceEditText);
    }

    public void bind(final ShoppingItem shoppingItem){
        productName.setText(shoppingItem.productName);
        boughtSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boughtSwitch.setText("Köpt!");
                    shoppingItem.bought = true;
                    ref.child(shoppingItem.pushKey).child("bought").setValue(true);
                } else {
                    boughtSwitch.setText("Köpt?");
                    shoppingItem.bought = false;
                    ref.child(shoppingItem.pushKey).child("bought").setValue(false);
                }


            }
        });

        boughtSwitch.setChecked(shoppingItem.bought);

        //when an item is removed, the value is drawn from the total sum variable and then updated in the database
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(shoppingItem.pushKey).removeValue();
                totalSumObj.drawFromTotalSum(shoppingItem.getPrice());

                ref.getParent().child("totalSum").setValue(totalSumObj.getTotalSum());
            }
        });

        priceTag.setText(String.valueOf(shoppingItem.getPrice()));

//        priceTag.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(context, "beforetextchanged"  + s.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                try {
////                    int number = Integer.parseInt(s.toString());
////                    ref.child(shoppingItem.pushKey).child("price").setValue(number);
////                    shoppingItem.setPrice(number);
//                    Toast.makeText(context, "ontextchange"  + s.toString(), Toast.LENGTH_SHORT).show();
////                } catch (NumberFormatException e) {}
//            }
//
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    if (priceTag.getText().hashCode() == s.hashCode()) {
//                        Toast.makeText(context, "aftertextchanged" + s.toString(), Toast.LENGTH_SHORT).show();
//                        int number = Integer.parseInt(s.toString());
//                        ref.child(shoppingItem.pushKey).child("price").setValue(number);
//                        shoppingItem.setPrice(number);
//
//                        totalSumObj.addTototalSum(number);
//                    }
//                } catch (NumberFormatException e) {}
//            }
//        });

        //If price needs to be edited, the previous price is reduced from the total sum on focus.
        priceTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    String priceString = priceTag.getText().toString();
                    int focusPriceInt = Integer.parseInt(priceString);
                    totalSumObj.drawFromTotalSum(focusPriceInt);
                    ref.getParent().child("totalSum").setValue(totalSumObj.getTotalSum());
                }
            }
        });

        //the price of an item is added to the totalsum and firebase when user enters a number and hits enter
        priceTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean enterWasPressed = event == null
                        || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN);
                if (enterWasPressed) {
                    String priceString = priceTag.getText().toString();
                    if (priceString.equals("")) {
                        priceInt = 0;
                        ref.child(shoppingItem.pushKey).child("price").setValue(priceInt);

                        totalSumObj.addTototalSum(priceInt);
                    } else {
                        priceInt = Integer.parseInt(priceString);
                        ref.child(shoppingItem.pushKey).child("price").setValue(priceInt);
                        totalSumObj.addTototalSum(priceInt);
                        ref.getParent().child("totalSum").setValue(totalSumObj.getTotalSum()); //add the totalSum to the totalSum-node in Firebase
                        priceTag.clearFocus(); //To avoid 'onFocusChange' after a user enters a number we clear the focus
                    }
                    return true;

                }

                return false;
            }
        });




    }

}
