package com.example.managementstudent;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);

        List<Student> studentList = new ArrayList<Student>();

        studentList.add(new Student("fsad","Ho Dang Thang", null, "10A3", "22/09/2002", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","fsadfsd", null, "11A3", "22/09/2001", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","Ho Dang Thang", null, "10A3", "22/09/2002", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","fsadfsd", null, "11A3", "22/09/2001", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","Ho Dang Thang", null, "10A3", "22/09/2002", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","fsadfsd", null, "11A3", "22/09/2001", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","Ho Dang Thang", null, "10A3", "22/09/2002", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));
        studentList.add(new Student("fsdf","fsadfsd", null, "11A3", "22/09/2001", "E:/tena/ManagementStudent /app/src/main/res/drawable/person.png"));

        RecyclerView recyclerView = findViewById(R.id.rcvStudent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(new StudentAdapter(getApplicationContext(), studentList));
    }
}