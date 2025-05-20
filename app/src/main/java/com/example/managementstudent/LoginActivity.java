package com.example.managementstudent;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;

    EditText edtPassword, edtUsername;
    private FirebaseAuth mAuth;
    LottieAnimationView lottieLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        init();
        setEdtPassword();
        login();

    }

    public void init() {
        btnLogin = findViewById(R.id.btnLogin);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        mAuth = FirebaseAuth.getInstance();
        lottieLoading = findViewById(R.id.lottieLoading);
    }

    public void onStart() {
        super.onStart();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setEdtPassword() {
        edtPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Index của drawableEnd
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    // Đảo trạng thái hiện/ẩn mật khẩu
                    if (edtPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    // Đặt con trỏ về cuối chuỗi
                    edtPassword.setSelection(edtPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    public void login() {
        btnLogin.setOnClickListener(v -> {
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            // Hiện loading
            lottieLoading.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);

            if (email.equals("admin") && password.equals("admin")) {
                Log.d(TAG, "You're admin");
                lottieLoading.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    lottieLoading.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    return;
                } else {
                    if (!isValidEmail(email)) {
                        Toast.makeText(LoginActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                        lottieLoading.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        return;
                    } else {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "SignInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                lottieLoading.setVisibility(View.GONE);
                                btnLogin.setEnabled(true);

                                Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                            } else {
                                Log.d(TAG, "SignInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}