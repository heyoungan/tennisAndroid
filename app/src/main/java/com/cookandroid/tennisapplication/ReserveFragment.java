package com.cookandroid.tennisapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashMap;
import java.util.Map;

public class ReserveFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private ViewGroup timeGridLayout;
    private TextView personCountTextView;
    private int personCount = 0;

    private FirebaseFirestore db;

    public ReserveFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        timeGridLayout = view.findViewById(R.id.timeGridLayout);
        personCountTextView = view.findViewById(R.id.personCountTextView);
        Button increasePersonButton = view.findViewById(R.id.increasePersonButton);
        Button decreasePersonButton = view.findViewById(R.id.decreasePersonButton);
        Button reserveButton = view.findViewById(R.id.reserveButton);

        // Firebase Firestore 초기화
        db = FirebaseFirestore.getInstance();

        // 캘린더 뷰 설정
        CalendarDay today = CalendarDay.today();
        calendarView.setSelectedDate(today);
        calendarView.setCurrentDate(today);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (CalendarDay.today().equals(date)) {
                Toast.makeText(getContext(), "당일 예약은 불가능합니다.", Toast.LENGTH_SHORT).show();
            } else if (date.isBefore(CalendarDay.today())) {
                Toast.makeText(getContext(), "지난 날짜는 예약이 불가능합니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "선택된 날짜: " + date.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // 시간 버튼 설정
        setupTimeButtons();

        // 인원수 버튼 설정
        increasePersonButton.setOnClickListener(v -> {
            if (personCount < 4) {
                personCount++;
                personCountTextView.setText(String.valueOf(personCount));
            } else {
                Toast.makeText(getContext(), "최대 인원은 4명까지 수용 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        decreasePersonButton.setOnClickListener(v -> {
            if (personCount > 0) {
                personCount--;
                personCountTextView.setText(String.valueOf(personCount));
            }
        });

        reserveButton.setOnClickListener(v -> {
            if (calendarView.getSelectedDate() == null) {
                Toast.makeText(getContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedTime = getSelectedTime();

            if (selectedTime == null) {
                Toast.makeText(getContext(), "시간을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int finalPersonCount = personCount;
            CalendarDay selectedDate = calendarView.getSelectedDate();

            // 파이어베이스 Firestore에 예약 정보 저장
            Map<String, Object> reservation = new HashMap<>();
            reservation.put("date", selectedDate.getDate().toString());
            reservation.put("time", selectedTime);
            reservation.put("personCount", finalPersonCount);

            db.collection("reservations").add(reservation)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // 성공적으로 예약이 완료되면 MyPageFragment로 이동
                        if (getActivity() != null && getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).navigateToMyPageFragmentWithReservation(reservation);
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "예약에 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        return view;
    }

    private void setupTimeButtons() {
        String[] times = {"12:00", "14:00", "16:00"};
        timeGridLayout.removeAllViews();  // 기존 버튼 제거
        for (String time : times) {
            Button button = new Button(getContext());
            button.setText(time);
            button.setOnClickListener(v -> {
                Toast.makeText(getContext(), "선택된 시간: " + time, Toast.LENGTH_SHORT).show();
                updateButtonStyles(time);
            });
            timeGridLayout.addView(button);
        }
    }

    private void updateButtonStyles(String selectedTime) {
        for (int i = 0; i < timeGridLayout.getChildCount(); i++) {
            Button btn = (Button) timeGridLayout.getChildAt(i);
            if (btn.getText().toString().equals(selectedTime)) {
                btn.setSelected(true);
                btn.setBackgroundColor(getResources().getColor(R.color.blue));
                btn.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn.setSelected(false);
                btn.setBackgroundColor(getResources().getColor(R.color.light_gray));
                btn.setTextColor(getResources().getColor(R.color.blue));
            }
        }
    }

    private String getSelectedTime() {
        for (int i = 0; i < timeGridLayout.getChildCount(); i++) {
            Button btn = (Button) timeGridLayout.getChildAt(i);
            if (btn.isSelected()) {
                return btn.getText().toString();
            }
        }
        return null;
    }
}
