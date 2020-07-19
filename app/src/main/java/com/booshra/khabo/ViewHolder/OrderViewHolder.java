package com.booshra.khabo.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.booshra.khabo.Interface.ItemClickListener;
import com.booshra.khabo.R;

public class OrderViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress,txtPaymentMethod;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtPaymentMethod = itemView.findViewById(R.id.payment_status);

        //itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
