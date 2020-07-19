package com.booshra.khabo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.booshra.khabo.Common.Common;
import com.booshra.khabo.Database.Database;
import com.booshra.khabo.Model.Order;
import com.booshra.khabo.Model.Request;
import com.booshra.khabo.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //initialize
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice =findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new request
                showAlertDialog();
            }
        });
        
        loadListfood();

    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step!");
        alertDialog.setMessage("Enter Your Address:  ");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.rder_address_comment,null);

        final MaterialEditText editAddress = (MaterialEditText) order_address_comment.findViewById(R.id.edit_address);

        final RadioButton radCOD = order_address_comment.findViewById(R.id.radCOD);
        final RadioButton radCard = order_address_comment.findViewById(R.id.radCard);

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //submit address to Firebase
                //System.currentMillis to key


                if(!radCOD.isChecked() && !radCard.isChecked()){
                    Toast.makeText(Cart.this, "Please select Option", Toast.LENGTH_SHORT).show();
                }
                else if (radCard.isChecked()){
                    showAlertDialog2();
                    //Toast.makeText(Cart.this, "You will Be Called", Toast.LENGTH_SHORT).show();
                }
                else{
                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            editAddress.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            "0",
                            "Cash On Delivery",
                            "Due",
                            cart
                    );
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                    Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                    new Database(getBaseContext()).cleanCart();
                    finish();
                }
                //delete cart

            }

            private void showAlertDialog2() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
                alertDialog.setTitle("Credit Card Info");
                alertDialog.setMessage("Enter Your Card No:  ");

                final EditText editCard = new EditText(Cart.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                editCard.setLayoutParams(lp);
                alertDialog.setView(editCard); //add edit text to alertDialog
                alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getName(),
                                editAddress.getText().toString(),
                                txtTotalPrice.getText().toString(),
                                "0",
                                "Card Payment",
                                "Payed",
                                cart
                        );
                        //submit address to Firebase
                        //System.currentMillis to key
                        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                        //delete cart
                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void loadListfood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //calculate price
        int total=0;
        for (Order order:cart)
            total=total+(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        total=total+50;
        Locale locale = new Locale("en","BD");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }
}
