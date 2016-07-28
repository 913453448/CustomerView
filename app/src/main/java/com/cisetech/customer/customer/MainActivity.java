package com.cisetech.customer.customer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.cisetech.customer.customer.View.TabLayout2;
import com.cisetech.customer.customer.View.Tag;
import com.cisetech.customer.customer.View.TagAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements TabLayout2.OnTagClickListener {
    private TabLayout2 tag;
    private List<Tag> datas=new ArrayList<Tag>();
    private TagAdapter<Tag>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tag= (TabLayout2) findViewById(R.id.id_tab_view);
        initData();
    }

    private void initData() {
        for (int i = 0; i <100 ; i++) {
            Tag tag=new Tag();
            tag.setName("Android Studio"+i);
            datas.add(tag);
        }
        adapter=new TagAdapter<Tag>(datas) {
            @Override
            protected View getView(TabLayout2 parent, int position, final Tag tag) {
                CheckedTextView view=new CheckedTextView(MainActivity.this);
                view.setBackgroundResource(R.drawable.selected_tag);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
                view.setTextColor(Color.WHITE);
                ViewGroup.MarginLayoutParams lp=new ViewGroup.MarginLayoutParams(-2,-2);
                lp.setMargins(4,4,4,4);
                view.setText(tag.getName());
                view.setLayoutParams(lp);
                return view;
            }
        };
        tag.setAdapter(adapter);
        tag.setOnTagClickListener(this);
    }

    @Override
    public void onTagClick(View view, int position, TabLayout2 parent, boolean isChoice) {
        if(isChoice){
            Toast.makeText(MainActivity.this, datas.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
