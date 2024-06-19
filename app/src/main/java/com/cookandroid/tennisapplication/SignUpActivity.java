package com.cookandroid.tennisapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewResult;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        textViewResult = findViewById(R.id.textViewResult);

        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                signUp(email, password);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();  // 회원가입 성공 후 로그아웃 수행
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            textViewResult.setText("비밀번호가 너무 약합니다.");
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            textViewResult.setText("이메일 형식이 올바르지 않습니다.");
                        } catch (FirebaseAuthUserCollisionException e) {
                            textViewResult.setText("이미 존재하는 이메일입니다.");
                        } catch (Exception e) {
                            textViewResult.setText("회원가입 실패: " + e.getMessage());
                        }
                    }
                });
    }
}
