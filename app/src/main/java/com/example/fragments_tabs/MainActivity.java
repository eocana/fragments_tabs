package com.example.fragments_tabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;

import com.example.fragments_tabs.view.MapFragment;
import com.example.fragments_tabs.view.WeatherFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new WeatherFragment());

        FragmentStateAdapter adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setIcon(R.drawable.ic_map);
                tab.setText(R.string.tab_map);
            } else {
                tab.setIcon(R.drawable.ic_text);
                tab.setText(R.string.tab_text);
            }
        }).attach();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentActivity fa, List<Fragment> fragmentList) {
            super(fa);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}
