package com.example.mymovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymovieapp.databinding.ActivityMainBinding;
import com.example.mymovieapp.databinding.RecyclerviewItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ActivityMainBinding binding;
    public static int PAGE_NUMBER=1;
    final Context mCtx=this;
    List<Result> items = new ArrayList<>();
    List<Result> items_temp= new ArrayList<>();


    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SearchView simpleSearchView = (SearchView) findViewById(R.id.simpleSearchView); // inititate a search view
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });


        CharSequence query = simpleSearchView.getQuery();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);

        RetrofitClient.getInstance()
                .getApi()
                .getTopRatedMovies("11459cff1c1ce00e3202addab99f3a91","en-us",PAGE_NUMBER)
                .enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        if(response!=null)
                        {
                            for (int i=0;i<response.body().results.size();i++)
                            {
                                items.add(response.body().results.get(i));
                                items_temp.add(response.body().results.get(i));
                            }

                            binding.recyclerView.setAdapter(new MyAdapter(items,mCtx));
                        }
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {

                    }
                });
        PAGE_NUMBER=PAGE_NUMBER+1;
    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() ==0)
        {
           for(int t=0;t<items_temp.size();t++)
           {
               items.add(items_temp.get(t));
           }
        }
        else if(charText.length()>=3)
        {
            for (int i = 0; i < items_temp.size(); i++) {
                if (items_temp.get(i).title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    items.add(items_temp.get(i));
                }
            }
        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void button_clicked(View view)
    {
        RetrofitClient.getInstance()
                .getApi()
                .getTopRatedMovies("11459cff1c1ce00e3202addab99f3a91","en-us",PAGE_NUMBER)
                .enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        if(response!=null)
                        {

                            for(int i=0;i<response.body().results.size();i++)
                            {
                                items.add(response.body().results.get(i));
                                items_temp.add(response.body().results.get(i));
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {

                    }
                });
        PAGE_NUMBER=PAGE_NUMBER+1;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private List<Result> items;

        Context mCtx;

        private class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView textView;
            RecyclerviewItemBinding binding;

            public MyViewHolder(RecyclerviewItemBinding b){

                super(b.getRoot());
                textView=b.textViewName;
                imageView=b.imageView;
                binding = b;
            }
        }

        public MyAdapter(List<Result> items, Context mCtx){
            this.items = items;
            this.mCtx=mCtx;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            return new MyViewHolder(RecyclerviewItemBinding.inflate(getLayoutInflater()));
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position){

            Glide.with(mCtx)
                    .load("https://image.tmdb.org/t/p/w500/"+items.get(position).poster_path)
                    .into(holder.imageView);

            String text = String.format(Locale.ENGLISH, "%s %d", items.get(position).title, position);
            holder.textView.setText(text);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PAGE_NUMBER=PAGE_NUMBER-1;
                    Intent intent = new Intent(mCtx,SecondActivity.class);
                    intent.putExtra("title",items.get(position).title);
                    intent.putExtra("poster_path",items.get(position).poster_path);
                    intent.putExtra("overview",items.get(position).overview);
                    intent.putExtra("popularity",items.get(position).popularity);
                    intent.putExtra("vote_average",items.get(position).vote_average);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount(){
            return items.size();
        }


    }
}