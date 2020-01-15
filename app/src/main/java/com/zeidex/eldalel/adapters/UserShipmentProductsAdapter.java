package com.zeidex.eldalel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zeidex.eldalel.R;
import com.zeidex.eldalel.listeners.OrderShipmentsActions;
import com.zeidex.eldalel.response.GetUserShipmentProducts;
import com.zeidex.eldalel.utils.ChangeLang;
import com.zeidex.eldalel.utils.PriceFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserShipmentProductsAdapter extends RecyclerView.Adapter<UserShipmentProductsAdapter.ShipmentProductHolder> {
    View view;
    private Context context;
    List<GetUserShipmentProducts.Order> shipmentProducts;
    OrderShipmentsActions mOrderShipmentsActions;
    CircularProgressDrawable mCircularProgressDrawable;

    public void setShipmentAction(OrderShipmentsActions orderShipmentsActions) {
        mOrderShipmentsActions = orderShipmentsActions;
    }

    public void setShipmentProducts(List<GetUserShipmentProducts.Order> shipmentProducts) {
        this.shipmentProducts = shipmentProducts;
        notifyDataSetChanged();
    }

    public UserShipmentProductsAdapter(Context context) {
        this.context = context;
        this.shipmentProducts = new ArrayList<>();
        mCircularProgressDrawable = new CircularProgressDrawable(context);
        mCircularProgressDrawable.setStrokeWidth(5f);
        mCircularProgressDrawable.setCenterRadius(30f);
        mCircularProgressDrawable.start();
    }

    @NonNull
    @Override
    public ShipmentProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_salesman_order_products_list_item, parent, false);
        final ShipmentProductHolder shipmentProductHolder = new ShipmentProductHolder(view);
        return shipmentProductHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShipmentProductHolder holder, int position) {
        GetUserShipmentProducts.Order userShipmentProduct = shipmentProducts.get(position);

        Locale locale = ChangeLang.getLocale(context.getResources());
        String loo = locale.getLanguage();
        if (loo.equalsIgnoreCase("ar")) {
            holder.order_product_name_tv.setText(userShipmentProduct.getProduct().getNameAr());
        } else {
            holder.order_product_name_tv.setText(userShipmentProduct.getProduct().getName());
        }

        holder.order_id_value_tv.setText(userShipmentProduct.getId() + "");
        double priceDouble = userShipmentProduct.getProduct().getPrice().doubleValue();
        holder.order_product_price_tv.setText(PriceFormatter.toDecimalRsString(priceDouble, context));
        holder.order_product_quantity_tv.setText(userShipmentProduct.getAvailableQuantity() + "");
        if (userShipmentProduct.getTotalPriceWithTax() != null) {
            double totalPriceDouble = userShipmentProduct.getTotalPriceWithTax().doubleValue();
            holder.order_product_total_price_value_tv.setText(PriceFormatter.toDecimalRsString(totalPriceDouble, context));
        }
        holder.order_date_tv.setText(userShipmentProduct.getCreatedAt() + "");

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable .setStrokeWidth(5f);
        circularProgressDrawable .setCenterRadius(30f);
        circularProgressDrawable .start();

        Glide.with(context)
                .load("https://dleel.com/homepages/get/" + userShipmentProduct.getProduct().getPhotos().get(0).getFilename())
                .placeholder(R.drawable.condition_logo)
//                .error(R.drawable.condition_logo)
                .centerCrop()
                .into(holder.order_product_iv);
    }

    @Override
    public int getItemCount() {
        return shipmentProducts.size() > 0 ? shipmentProducts.size() : 0;
    }

    public class ShipmentProductHolder extends RecyclerView.ViewHolder {
        public TextView order_product_name_tv, order_product_price_tv, order_product_quantity_tv, order_product_total_price_value_tv, order_date_tv, order_id_value_tv;
        public ImageView order_product_iv;
        public ImageButton deliverAcceptButton, deliverCancelButton, deliverChangeQuantityButton;

        public ShipmentProductHolder(View itemView) {
            super(itemView);
            order_product_name_tv = itemView.findViewById(R.id.order_product_name_tv);
            order_product_price_tv = itemView.findViewById(R.id.order_product_price_tv);
            order_product_quantity_tv = itemView.findViewById(R.id.order_product_quantity_tv);
            order_product_total_price_value_tv = itemView.findViewById(R.id.order_product_total_price_value_tv);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
            order_id_value_tv = itemView.findViewById(R.id.order_id_value_tv);
            order_product_iv = itemView.findViewById(R.id.order_product_iv);
            deliverAcceptButton = itemView.findViewById(R.id.deliver_accept_button);
            deliverCancelButton = itemView.findViewById(R.id.deliver_decline_buton);
            deliverChangeQuantityButton = itemView.findViewById(R.id.deliver_edit_button);

            deliverAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderShipmentsActions.onOrderShipmentDeliver(shipmentProducts.get(getAdapterPosition()).getId(), getAdapterPosition(), "user");
                }
            });

            deliverChangeQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderShipmentsActions.onOrderShipmentChangeQuantity(shipmentProducts.get(getAdapterPosition()).getId(), getAdapterPosition(), shipmentProducts.get(getAdapterPosition()).getAvailableQuantity(), "user");
                }
            });

            deliverCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderShipmentsActions.onOrderShipmentCancel(shipmentProducts.get(getAdapterPosition()).getId(), getAdapterPosition(), "user");
                }
            });
        }
    }

}
