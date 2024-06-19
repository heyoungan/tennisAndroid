package com.cookandroid.tennisapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_reserve) {
                selectedFragment = new ReserveFragment();
            } else if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_bulletin) {
                selectedFragment = new BulletinFragment();
            } else if (itemId == R.id.nav_mypage) {
                selectedFragment = new MyPageFragment();
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }

            return true;
        });

        // 기본 프래그먼트를 설정합니다.
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    public void navigateToMyPageFragmentWithReservation(Map<String, Object> reservation) {
        Log.d("MainActivity", "Navigating to MyPageFragment with reservation: " + reservation); // 로그 추가
        MyPageFragment myPageFragment = MyPageFragment.newInstance(reservation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, myPageFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // 하단 네비게이션의 선택된 항목을 mypage로 변경
        bottomNavigationView.setSelectedItemId(R.id.nav_mypage); // 수정된 부분
    }
}
