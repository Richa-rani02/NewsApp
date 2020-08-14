package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.news_model;
import welcome.example.newsapp.R;

public class newsAdapter  extends RecyclerView.Adapter<newsAdapter.ViewHolder>{
    private Context context;
    public List<news_model> newsdata;

    public newsAdapter(List<news_model> newsdata) {

        this.newsdata = newsdata;
    }

    @NonNull
    @Override
    public newsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newsitem = layoutInflater.inflate(R.layout.rv_data, parent, false);
        context = parent.getContext();
        return new ViewHolder(newsitem);
    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter.ViewHolder holder, int position) {

        news_model newdata = newsdata.get(position);
        String image=newdata.getUrlToImage();
        String author=newdata.getAuthor();
        if(image.isEmpty()){
            holder.cat_img.setImageResource(R.drawable.newsicon);
        }else{
            Picasso.with(context).load(newdata.getUrlToImage())

                    .into(holder.cat_img);
        }

        holder.title.setText(newdata.getTitle());
        holder.desc.setText(newdata.getDescription());
        holder.author.setText(newdata.getAuthor());
        holder.date.setText(newdata.getPublishedAt());

    }

    @Override
    public int getItemCount() {
        return newsdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView cat_img;
        public TextView title, desc, author,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_img=itemView.findViewById(R.id.cat_img);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            author=itemView.findViewById(R.id.author);
            date=itemView.findViewById(R.id.date);
        }
    }
}
