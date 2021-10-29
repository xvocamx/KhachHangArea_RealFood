package com.example.khachhangarea_realfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.ViewPaperYeuThichAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPaperYeuThichAdapter viewPaperYeuThichAdapter;

    public FavoriteFragment() {
    }


    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        setControl();
        setEvent();
        LoadTab();
        return view;
    }

    private void setEvent() {
    }

    private void LoadTab(){
        viewPaperYeuThichAdapter = new ViewPaperYeuThichAdapter(getActivity());
        viewPager.setAdapter(viewPaperYeuThichAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Food");
                        break;
                    case 1:
                        tab.setText("Shop");
                        break;
                }
            }
        }).attach();
    }

    private void setControl() {
        tabLayout = view.findViewById(R.id.tab_YeuThich);
        viewPager = view.findViewById(R.id.view_paper_yeuthich);
    }
}