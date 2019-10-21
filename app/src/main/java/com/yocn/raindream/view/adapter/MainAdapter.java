package com.yocn.raindream.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.yocn.raindream.R;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.utils.LogUtil;
import com.yocn.raindream.view.activity.PlayActivity;
import com.yocn.raindream.view.fragment.PlayFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author yocn
 * @Date 2019/8/4 10:24 AM
 * @ClassName MainAdapter
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_HEAD = 1;

    private Context mContext;
    private int[] colors = {R.color.color1, R.color.color2, R.color.color3
            , R.color.color4, R.color.color5, R.color.color6
            , R.color.color7, R.color.color8, R.color.color9};

    private int[] textColor = {R.color.write, R.color.black, R.color.write
            , R.color.black, R.color.black, R.color.black
            , R.color.write, R.color.black, R.color.write};

    private OnItemClickInterface mOnItemClickInterface;

    public interface OnItemClickInterface {
        void OnItemClick(JumpBean bean);
    }

    public void setOnItemClickInterface(OnItemClickInterface i) {
        mOnItemClickInterface = i;
    }

    public static List<JumpBean> getDataList() {
        List<JumpBean> list = new ArrayList<>();
        list.add(new JumpBean("场景1", PlayFragment.class, R.color.color1, R.color.write));
        list.add(new JumpBean("场景2", PlayFragment.class, R.color.color2, R.color.black));
        list.add(new JumpBean("场景3", PlayFragment.class, R.color.color3, R.color.write));
        list.add(new JumpBean("场景4", PlayFragment.class, R.color.color4, R.color.black));
        list.add(new JumpBean("场景5", PlayFragment.class, R.color.color5, R.color.black));
        list.add(new JumpBean("场景6", PlayFragment.class, R.color.color6, R.color.black));
        list.add(new JumpBean("场景7", PlayFragment.class, R.color.color7, R.color.write));
        list.add(new JumpBean("场景8", PlayFragment.class, R.color.color8, R.color.black));
        list.add(new JumpBean("场景9", PlayFragment.class, R.color.color9, R.color.write));
        return list;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public final TextView title;
        final RelativeLayout all;

        public VH(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            all = v.findViewById(R.id.all);
        }
    }

    public static class VHHeader extends RecyclerView.ViewHolder {
        VideoView videoView;

        public VHHeader(View v) {
            super(v);
//            videoView = v.findViewById(R.id.vv);
//            videoView.setVideoURI(Uri.parse("android.resource://com.yocn.media/" + R.raw.onboarding_bg));
//            videoView.start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_CONTENT;
        }
    }

    private List<JumpBean> mDatas;

    public MainAdapter(List<JumpBean> data) {
        this.mDatas = data;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_CONTENT) {
            VH holder = (VH) viewholder;
            holder.title.setText(mDatas.get(position - 1).getShow() + "");
            holder.title.setTextColor(mContext.getResources().getColor(textColor[(position - 1) % textColor.length]));
            holder.all.setBackgroundResource(colors[(position - 1) % colors.length]);
            holder.itemView.setOnClickListener(v -> {
                //item 点击事件
                LogUtil.d("realPosition    click-" + (position - 1) + "    " + mDatas.get((position - 1)).getToClass() + " position-》" + (position - 1));
                if (mOnItemClickInterface != null) {
                    mOnItemClickInterface.OnItemClick(mDatas.get(position - 1));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        //LayoutInflater.from指定写法
        if (viewType == TYPE_CONTENT) {
            holder = new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        } else {
            holder = new VHHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
        }
        return holder;
    }
}
