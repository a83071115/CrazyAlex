package com.alex.crazyalex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class ZhiHuDailyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  Context mContext;
    private  LayoutInflater mInflater;
    private List<ZhihuDailyNews.Question> list = new ArrayList<>();

    public void setOnItemClickListener(OnRecyclerViewOnClickListener listener) {
        mListener = listener;
    }

    public OnRecyclerViewOnClickListener mListener;
    //文字 + 图片 的类型
    private static final int TYPE_NORMAL = 0;
    //footer , 加载更多
    private static final int TYPE_FOOTER = 1;

    public ZhiHuDailyNewsAdapter(Context context,List<ZhihuDailyNews.Question> list) {
        this.mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 创建ViewHolder
     * @param parent Recycler 本身
     * @param viewType 传入的类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据ViewType加载不同布局
        switch (viewType){
            case TYPE_NORMAL:
                return new NormalViewHolder(mInflater.inflate(R.layout.home_list_item_layout,parent,false),mListener);
            case TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.list_footer,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //对不同的viewHolder做不同的处理
        if(holder instanceof NormalViewHolder){

            ZhihuDailyNews.Question item = list.get(position);

            if(item.getImages().get(0)==null){
                ((NormalViewHolder)holder).mImageView.setImageResource(R.drawable.placeholder);
            }else{
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .into(((NormalViewHolder)holder).mImageView);
            }
            ((NormalViewHolder)holder).mTextView.setText(item.getTitle());
        }
    }

    /**
     * 因为有footer,返回值需要+1
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    /**
     * 获取item类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ZhiHuDailyNewsAdapter.TYPE_FOOTER;
        }
        return ZhiHuDailyNewsAdapter.TYPE_NORMAL;
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView ;
        private OnRecyclerViewOnClickListener mListener;
        public NormalViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageViewCover);
            mTextView = (TextView) view.findViewById(R.id.textViewTitle);
            this.mListener = listener;
            view.setOnClickListener(this);
        }

        /**
         * recycler点击事件 , 通过接口实现
         * @param view
         */
        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.OnItemClick(view,getLayoutPosition());
            }
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }
}
