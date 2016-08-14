package com.cisetech.customer.customer.Animation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisetech.customer.customer.R;

import java.util.List;

/**
 * Author:Yqy
 * Date:2016-08-04
 * Desc:
 * Company:cisetech
 */
public class PinnerAdpater extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter, AbsListView.OnScrollListener {
    private List<User> mDatas;
    private Context context;
    public PinnerAdpater(Context context,List<User>mDatas){
        this.context=context;
        this.mDatas=mDatas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user= (User) getItem(position);
        if(convertView==null){
            convertView=View.inflate(context, R.layout.activity_custom_title_listview_section_item,null);
            convertView.setTag(""+position);
        }
        TextView title= (TextView) convertView.findViewById(R.id.header);
        TextView content= (TextView) convertView.findViewById(R.id.example_text_view);
        title.setText(user.getName());
        content.setText(user.getNumber());
        return convertView;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
    }
    private int lastItem;
    @Override
    public void configurePinnerHeader(View header, int position) {
        Log.e("PinnerAdapter----->", "configurePinnerHeader: "+position);
        if(lastItem!=position){
            //notifyDataSetChanged();
        }
        TextView text= (TextView) header.findViewById(R.id.header_text);
        text.setText(mDatas.get(position).getName());
        lastItem=position;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
        }
    }
}
