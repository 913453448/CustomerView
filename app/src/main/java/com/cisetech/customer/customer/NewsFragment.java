package com.cisetech.customer.customer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Author:Yqy
 * Date:2016-07-27
 * Desc:
 * Company:cisetech
 */
public class NewsFragment extends Fragment{
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title=getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view=new TextView(getActivity());
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14.5f);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.DKGRAY);
        view.setText(title);
        return view;
    }
}
