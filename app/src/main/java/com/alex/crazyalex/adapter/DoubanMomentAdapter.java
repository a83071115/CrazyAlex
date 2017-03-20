package com.alex.crazyalex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class DoubanMomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DoubanMomentNews.posts> list;
    private LayoutInflater mInflater;
    private static final int TYPE_NORMAL = 0x00;
    private static final int TYPE_FOOTER = 0x02;
    private static final int TYPE_NO_IMG = 0x03;

    OnRecyclerViewOnClickListener mListener;

    public void setListener(OnRecyclerViewOnClickListener listener) {
        mListener = listener;
    }

    public DoubanMomentAdapter(List<DoubanMomentNews.posts> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NORMAL:
                return new NormalViewHolder(mInflater.inflate(R.layout.home_list_item_layout,parent,false),mListener);
            case TYPE_NO_IMG:
                return new NoImgViewHolder(mInflater.inflate(R.layout.home_list_item_layout,parent,false),mListener);
            case TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.home_list_item_layout,parent,false),mListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断类型
        if(!(holder instanceof FooterViewHolder)){
            DoubanMomentNews.posts item = list.get(position);
            if(holder instanceof NormalViewHolder){
                Glide.with(mContext)
                        .load(item.getThumbs().get(0).getMedium().getUrl())
                        .asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .into(((NormalViewHolder) holder).ivHeadLineImg);
                ((NormalViewHolder) holder).tvTitle.setText(item.getTitle());
            } else if(holder instanceof NoImgViewHolder){
                ((NoImgViewHolder) holder).tvTitle.setText(item.getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position ==list.size()){
            return TYPE_FOOTER;
        }
        if(list.get(position).getThumbs().size()==0){
            return TYPE_NO_IMG;
        }
        return TYPE_NORMAL;
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivHeadLineImg;
        TextView tvTitle;
        OnRecyclerViewOnClickListener mListener;
        public NormalViewHolder(View inflate, OnRecyclerViewOnClickListener listener) {
            super(inflate);
            ivHeadLineImg = (ImageView) inflate.findViewById(R.id.imageViewCover);
            tvTitle = (TextView) inflate.findViewById(R.id.textViewTitle);
            this.mListener = listener;
            inflate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.OnItemClick(view,getLayoutPosition());
            }
        }
    }
    private class NoImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        OnRecyclerViewOnClickListener mListener;

        public NoImgViewHolder(View inflate, OnRecyclerViewOnClickListener listener) {
            super(inflate);
            tvTitle = (TextView) inflate.findViewById(R.id.textViewTitle);
            this.mListener = listener;
            inflate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.OnItemClick(view,getLayoutPosition());
            }
        }
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View inflate, OnRecyclerViewOnClickListener listener) {
            super(inflate);
        }
    }
}
