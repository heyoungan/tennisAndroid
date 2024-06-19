package com.cookandroid.tennisapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.Map;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";
    private static final String ARG_RESERVATION = "reservation";

    private FirebaseFirestore db;
    private LinearLayout reservationLayout;
    private Map<String, Object> reservation;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(Map<String, Object> reservation) {
        MyPageFragment fragment = new MyPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESERVATION, (Serializable) reservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            reservation = (Map<String, Object>) getArguments().getSerializable(ARG_RESERVATION);
            Log.d(TAG, "Received reservation: " + reservation);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        reservationLayout = view.findViewById(R.id.reservationLayout);

        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFirestoreListener();
    }

    private void setupFirestoreListener() {
        db.collection("reservations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (getContext() == null) {
                            return;
                        }

                        reservationLayout.removeAllViews(); // 기존 뷰 제거

                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getData() != null) {
                                addReservationView(doc);
                            }
                        }
                    }
                });
    }

    private void addReservationView(QueryDocumentSnapshot doc) {
        if (getContext() == null) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View reservationView = inflater.inflate(R.layout.item_reservation, reservationLayout, false);

        TextView textView = reservationView.findViewById(R.id.reservationDetails);
        Button cancelButton = reservationView.findViewById(R.id.cancelButton);

        textView.setText("예약 내용:\n날짜: " + doc.getString("date") +
                "\n시간: " + doc.getString("time") +
                "\n인원: " + doc.getLong("personCount"));

        cancelButton.setOnClickListener(v -> {
            db.collection("reservations").document(doc.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "예약 취소에 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        reservationLayout.addView(reservationView);
    }
}
