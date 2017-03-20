package com.alex.crazyalex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class GuokrNewsAdapter extends RecyclerView.Adapter<GuokrNewsAdapter.GuokrPostViewHolder> {
    private Context content;
    private List<GuokrHandpickNews.result> list;
    private LayoutInflater inflater;

    public void setListener(OnRecyclerViewOnClickListener listener) {
        mListener = listener;
    }

    private OnRecyclerViewOnClickListener mListener;


    public GuokrNewsAdapter(Context content, ArrayList<GuokrHandpickNews.result> list) {
        this.content = content;
        this.list = list;
        inflater = LayoutInflater.from(content);
    }

    @Override
    public GuokrPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_list_item_layout,parent,false);

        return new GuokrPostViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(GuokrPostViewHolder holder, int position) {
        GuokrHandpickNews.result item = list.get(position);

        Glide.with(content)
                .load(item.getHeadline_img_tb())
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(holder.ivHeadlineImg);
        holder.tvTitle.setText(item.getTitle());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GuokrPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivHeadlineImg;
        TextView tvTitle;
        OnRecyclerViewOnClickListener mListener;
        public GuokrPostViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            ivHeadlineImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvTitle = (TextView) itemView.findViewById(R.id.textViewTitle);

            this.mListener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.OnItemClick(view,getLayoutPosition());
            }
        }
    }
}
