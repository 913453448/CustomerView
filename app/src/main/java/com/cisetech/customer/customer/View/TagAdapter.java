package com.cisetech.customer.customer.View;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:Yqy
 * Date:2016-07-28
 * Desc:
 * Company:cisetech
 */
public abstract class TagAdapter<T> {
    public interface OnDataChangedListener{
        void onChange();
    }
    private List<T>mTagDatas;
    private OnDataChangedListener onDataChangedListener;

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    public TagAdapter( List<T>mTagDatas){
        this.mTagDatas=mTagDatas;
    }
    public TagAdapter( T[]mTagDatas){
        this.mTagDatas=new ArrayList<T>(Arrays.asList(mTagDatas));
    }
    public int getCount(){
        return mTagDatas==null?0:mTagDatas.size();
    }
    public T getItem(int position){
        return mTagDatas.get(position);
    }
    public void notifyDataChanged(){
        if(onDataChangedListener!=null){
            onDataChangedListener.onChange();
        }
    }
    protected  abstract View getView(TabLayout2 parent,int position,T t);

}
