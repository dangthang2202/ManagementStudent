package com.example.managementstudent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementstudent.adapter.StudentAdapter;
import com.example.managementstudent.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    RecyclerView rcvStudent;
    EditText edtSearch;

    FirebaseFirestore db;
    CollectionReference studentRef;

    List<Student> studentList;
    StudentAdapter studentAdapter;
    List<Student> fullStudentList; // Dùng cho tìm kiếm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);

        init();
        setEdtSearch();
        listenToStudentChanges();
    }

    private void init() {
        rcvStudent = findViewById(R.id.rcvStudent);
        edtSearch = findViewById(R.id.edtSearch);

        db = FirebaseFirestore.getInstance();
        studentRef = db.collection("students");

        studentList = new ArrayList<>();
        fullStudentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(getApplicationContext(), studentList);

        rcvStudent.setLayoutManager(new LinearLayoutManager(this));
        rcvStudent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvStudent.setAdapter(studentAdapter);
    }

    private void setEdtSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String keyword) {
        studentList.clear();
        for (Student student : fullStudentList) {
            if (student.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    student.getClassName().toLowerCase().contains(keyword.toLowerCase()) ||
                    student.getBirthDay().toLowerCase().contains(keyword.toLowerCase())) {
                studentList.add(student);
            }
        }
        studentAdapter.notifyDataSetChanged();
    }

    private void listenToStudentChanges() {
        studentRef.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed.", e);
                return;
            }

            if (querySnapshot != null) {
                fullStudentList.clear();
                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    Student student = doc.toObject(Student.class);
                    fullStudentList.add(student);
                }

                // Nếu không có tìm kiếm, hiển thị toàn bộ
                if (edtSearch.getText().toString().isEmpty()) {
                    studentList.clear();
                    studentList.addAll(fullStudentList);
                    studentAdapter.notifyDataSetChanged();
                } else {
                    // Giữ kết quả lọc hiện tại khi DB thay đổi
                    filterList(edtSearch.getText().toString());
                }
            }
        });
    }
}
