package com.cookandroid.tennisapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class BulletinFragment extends Fragment {

    private RecyclerView recyclerView;
    private BulletinAdapter adapter;
    private List<Bulletin> bulletins;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bulletin, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bulletins = new ArrayList<>();
        bulletins.add(new Bulletin("한성대학교 테니스 대회 안내 (2024년 6월)", "2024.05.16", "2024년 6월 테니스 대회 안내\n\n2024.06.10 ( 여자 SILVER )\n시간: 오후 6:00\n종목: 단식\n인원: 2명\n\n2024.06.28 ( 남자 SILVER )\n시간: 오전 10:00\n종목: 복식\n인원: 4명\n\n참가를 원하시는 분들은 신청 후 대회 날짜에 맞춰 준비해 주세요."));
        bulletins.add(new Bulletin("6월 연휴 휴관 안내", "2024.05.08", "6일(목) 현충일\n\n위의 날짜에 휴관합니다.\n참고 부탁드립니다."));
        // 더 많은 공지사항 데이터를 추가하세요

        adapter = new BulletinAdapter(getContext(), bulletins);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
