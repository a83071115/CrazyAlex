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
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import static com.alex.crazyalex.R.id.textViewType;

/**
 * Created by Administrator on 2017/3/21.
 */

public class BookmarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  Context context;
    private LayoutInflater inflater;

    private ArrayList<ZhihuDailyNews.Question> zhihuList;

    private ArrayList<GuokrHandpickNews.result> guokrList;

    private ArrayList<DoubanMomentNews.posts> doubanList;
    //给RecyclerView显示的数据
    private ArrayList<Integer> type ;

    private OnRecyclerViewOnClickListener mListener;

    public void setListener(OnRecyclerViewOnClickListener listener) {
        mListener = listener;
    }

    public static final int TYPE_ZHIHU_NORMAL = 0;
    public static final int TYPE_ZHIHU_WITH_HEADER = 1;
    public static final int TYPE_GUOKR_NORMAL = 2;
    public static final int TYPE_GUOKR_WITH_HEADER = 3;
    public static final int TYPE_DOUBAN_NORMAL = 4;
    public static final int TYPE_DOUBAN_WITH_HEADER = 5;


    public BookmarsAdapter(Context context
                                ,ArrayList<ZhihuDailyNews.Question> zhihuList
                                ,ArrayList<GuokrHandpickNews.result> guokrList
                                ,ArrayList<DoubanMomentNews.posts> doubanList
                                ,ArrayList<Integer> type
    ){
        this.context = context;
        this.zhihuList = zhihuList;
        this.guokrList = guokrList;
        this.doubanList = doubanList;
        inflater = LayoutInflater.from(context);
        //type.size = zhihuList.size + guokrList.size + doubanList.size;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            //如果是这三个类型 都走下面共同的布局 还有viewHolder
            case TYPE_ZHIHU_NORMAL:
            case TYPE_GUOKR_NORMAL:
            case TYPE_DOUBAN_NORMAL:
                return new NormalViewholder(inflater.inflate(R.layout.home_list_item_layout,parent,false),this.mListener);

        }
        //其他的全部加载空布局
        return new WithTypeViewHolder(inflater.inflate(R.layout.bookmark_header,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (type.get(position)){
            case TYPE_ZHIHU_WITH_HEADER:
                ((WithTypeViewHolder)holder).mTextView.setText(R.string.zhihu_daily);
                break;
            case TYPE_ZHIHU_NORMAL:
                if(!zhihuList.isEmpty()){
                    //因为前面的位置有人占了 所以加载的时候需要-1

                    ZhihuDailyNews.Question q = zhihuList.get(position-1);
                    Glide.with(context)
                            .load(q.getImages().get(0))
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .into(((NormalViewholder)holder).imageView);
                    ((NormalViewholder)holder).textViewTitle.setText(q.getTitle());
                }
                break;
            case TYPE_GUOKR_WITH_HEADER:
                ((WithTypeViewHolder)holder).mTextView.setText(R.string.guokr_handpick);
                break;
            case TYPE_GUOKR_NORMAL:
                if(!guokrList.isEmpty()){
                    //还是因为前面有行 所以 位置要减去 上面的头和 zhihuList显示多少行在-2
                    GuokrHandpickNews.result r = guokrList.get(position - zhihuList.size() - 2);
                    Glide.with(context)
                            .load(r.getHeadline_img_tb())
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .into(((NormalViewholder)holder).imageView);
                    ((NormalViewholder)holder).textViewTitle.setText(r.getTitle());
                }
                break;
            case TYPE_DOUBAN_WITH_HEADER:
                ((WithTypeViewHolder)holder).mTextView.setText(R.string.douban_moment);
                break;
            case TYPE_DOUBAN_NORMAL:
                if(!doubanList.isEmpty()){
                    //position = position - zhuhuList.size - guokrList.size - 3
                    DoubanMomentNews.posts posts = doubanList.get(position - zhihuList.size() - guokrList.size() - 3);
                    if(posts.getThumbs().size()==0){
                        ((NormalViewholder)holder).imageView.setVisibility(View.VISIBLE);
                    }else{
                        Glide.with(context)
                                .load(posts.getThumbs().get(0).getMedium().getUrl())
                                .asBitmap()
                                .placeholder(R.drawable.placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .error(R.drawable.placeholder)
                                .centerCrop()
                                .into(((NormalViewholder)holder).imageView);
                        ((NormalViewholder)holder).textViewTitle.setText(posts.getTitle());
                    }
                }
                break;



        }
    }

    @Override
    public int getItemCount() {
        return type.size();
    }

    @Override
    public int getItemViewType(int position) {
        return type.get(position);
    }

    class NormalViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewTitle;
        OnRecyclerViewOnClickListener mListener;
        public NormalViewholder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            itemView.setOnClickListener(this);
        }




        @Override
        public void onClick(View view) {
            if(mListener!=null){

                mListener.OnItemClick(view,getLayoutPosition());
            }
        }
    }

    private class WithTypeViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView ;
        public WithTypeViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(textViewType);
        }
    }
}
