package com.booshra.khabo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.booshra.khabo.Interface.ItemClickListener;
import com.booshra.khabo.Model.Food;
import com.booshra.khabo.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId="";
    FirebaseRecyclerOptions<Food> options;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FOR_LOG2", "inside oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("food");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get Intent
            categoryId = getIntent().getStringExtra("CategoryId");
         if(!categoryId.isEmpty() && categoryId !=null){
            loadListFood(categoryId);}
    }

    private void loadListFood(String categoryId) {
        Log.d("FOR_LOG2", "inside loadfood");
        //create query by name
        Query searchByKey = foodList.orderByChild("MenuId").equalTo(categoryId);
        Log.d("FOR_LOG2", categoryId);
        //Create options with query
        options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByKey,Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                Log.d("FOR_LOG2", model.getName());
                Picasso.get().load(model.getImage()).into(holder.food_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(FoodList.this, "Could not get the image", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.food_name.setText(model.getName());
                Log.d("FOR_LOG2", model.getName());
                final Food local = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent foodDetail = new Intent(FoodList.this,Food_Detail.class);
                        foodDetail.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });

            }


            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item,viewGroup,false);

                return new FoodViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}
