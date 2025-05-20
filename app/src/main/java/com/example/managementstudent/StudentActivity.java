package com.example.managementstudent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementstudent.adapter.StudentAdapter;
import com.example.managementstudent.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    RecyclerView rcvStudent;

    FirebaseFirestore db;
    CollectionReference studentRef;
    EditText edtSearch;

    List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);
        init();
        setEdtSearch();
        readStudent();
        createStudent();

    }

    public void init() {
        rcvStudent = findViewById(R.id.rcvStudent);
        db = FirebaseFirestore.getInstance();
        edtSearch = findViewById(R.id.edtSearch);
        studentList = new ArrayList<>();
        studentRef = db.collection("students");
        setRcvStudent();
    }

    public void setEdtSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterList(String keyword) {
        List<Student> filteredList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                student.getClassName().toLowerCase().contains(keyword.toLowerCase()) ||
                student.getBirthDay().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(student);
            }
        }

        // Cập nhật lại danh sách trong adapter
        rcvStudent.setAdapter(new StudentAdapter(getApplicationContext(), filteredList));
    }

    private void readStudent() {
        studentRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Student student = doc.toObject(Student.class);
                        studentList.add(student);
                    }
                    // TODO: Gán studentList vào RecyclerView
                    setRcvStudent();
                });

    }

    private void createStudent() {
        String id = studentRef.document().getId(); // ID tự động
        Student student = new Student(
                null,
                "Nguyễn Văn A",
                "CNTT1",
                "01/01/2000",
                "Lớp trưởng",
                "Công nghệ thông tin",
                "Kỹ thuật phần mềm",
                "https://firebasestorage.googleapis.com/v0/b/managerstudent-a8bde.firebasestorage.app/o/images.png?alt=media&token=41e78e61-0166-48d9-8b62-ce2fcec9cead",
                new ArrayList<>()
        );
        student.setId(id); // Gán ID vào model

        studentRef.document(id).set(student)
                .addOnSuccessListener(unused -> Log.d("Firestore", "Student added"))
                .addOnFailureListener(e -> Log.e("Firestore", "Add failed", e));
    }

    private void deleteStudent() {
        String studentId = "abc123";

        studentRef.document(studentId)
                .delete()
                .addOnSuccessListener(unused -> Log.d("Firestore", "Deleted"))
                .addOnFailureListener(e -> Log.e("Firestore", "Delete failed", e));
    }

    private void setRcvStudent() {
        rcvStudent.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(rcvStudent.getContext(), DividerItemDecoration.VERTICAL);
        rcvStudent.addItemDecoration(divider);
        rcvStudent.setAdapter(new StudentAdapter(getApplicationContext(), studentList));
    }

    private void autoID() {

    }
}