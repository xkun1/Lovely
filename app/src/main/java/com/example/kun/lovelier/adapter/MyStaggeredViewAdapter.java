package com.example.kun.lovelier.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kun.lovelier.R;
import com.example.kun.lovelier.dialog.ImageDiaLog;
import com.example.kun.lovelier.moudle.DataBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kun on 2016/4/20.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<DataBean.NewslistBean> mDatas; //数据源
    public List<Integer> mHeights;  //item高度
    public LayoutInflater mLayoutInflater;


    public MyStaggeredViewAdapter(Context mContext, List<DataBean.NewslistBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (Math.random() * 300) + 200);
        }

    }


    //创建viewHolder
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_layout, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
        return mViewHolder;
    }

    //绑定viewHodler
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        if (mHeights.size() > 0) {
            params.height = mHeights.get(position);//把随机的高度赋予itemView布局
            holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        }


        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });

        }


        holder.imgtitle.setText(mDatas.get(position).getTitle());

        Glide.with(mContext)
                .load(mDatas.get(position).getPicUrl())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView imgtitle;
    public RoundedImageView img;
    public RelativeLayout layout;

    @SuppressLint("WrongViewCast")
    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        imgtitle = (TextView) itemView.findViewById(R.id.img_title);
        img = (RoundedImageView) itemView.findViewById(R.id.img);
        layout = (RelativeLayout) itemView.findViewById(R.id.layout);
    }
}
