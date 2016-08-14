package com.cisetech.customer.customer.Animation;

import android.content.Context;
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
public class PinnerAdapter2 extends BaseAdapter implements PinnedHeaderListView2.OnHeaderViewChangeListener,AbsListView.OnScrollListener{
    private Context mContext;
    private List<User>datas;
    public PinnerAdapter2(Context context,List<User> datas){
        this.mContext=context;
        this.datas=datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user= (User) getItem(position);
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.activity_custom_title_listview_section_item,null);
        }
        TextView title= (TextView) convertView.findViewById(R.id.header);
        TextView content= (TextView) convertView.findViewById(R.id.example_text_view);
        title.setText(user.getName());
        content.setText(user.getNumber());
        return convertView;
    }
    private int lastItem;
    @Override
    public void onHeaderChange(View headerView, int position) {
        if(lastItem!=position){
            //notifyDataSetChanged();
        }
        TextView text= (TextView) headerView.findViewById(R.id.header_text);
        text.setText(datas.get(position).getName());
        lastItem=position;
    }

    @Override
    public int getChangeState() {
        return PinnedHeaderListView2.OnHeaderViewChangeListener.PINNED_HEADER_PUSHED_UP;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(view instanceof PinnedHeaderListView2){
            ((PinnedHeaderListView2)view).configuraPinner(firstVisibleItem);
        }
    }
}
