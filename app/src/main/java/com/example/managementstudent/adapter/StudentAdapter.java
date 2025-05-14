package com.example.managementstudent.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementstudent.R;
import com.example.managementstudent.model.Student;
import com.example.managementstudent.viewHolder.StudentViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    Context context;
    List<Student> studentList;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.imvStudent.setImageURI(Uri.parse(studentList.get(position).getImgStudent()));

        holder.tvAgeStudent.setText(calculateAge(studentList.get(position).getBirthDay()) + "");
        holder.tvNameStudent.setText(studentList.get(position).getName());
        holder.tvClassStudent.setText(studentList.get(position).getClassName());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static int calculateAge(String dobString) {
        try {
            // Định dạng ngày sinh: "dd/MM/yyyy"
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",new Locale("vi", "VN"));
            Date birthDate = sdf.parse(dobString);

            // Chuyển sang LocalDate để tính tuổi
            assert birthDate != null;
            LocalDate birthLocalDate = birthDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate currentDate = LocalDate.now();
            return Period.between(birthLocalDate, currentDate).getYears();

        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Lỗi định dạng
        }
    }
}
