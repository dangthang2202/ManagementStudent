package com.example.managementstudent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.managementstudent.DetailStudentActivity;
import com.example.managementstudent.R;
import com.example.managementstudent.StudentActivity;
import com.example.managementstudent.model.Student;
import com.example.managementstudent.viewHolder.StudentViewHolder;
import com.google.firebase.firestore.FirebaseFirestore;

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
        Student student = studentList.get(position);
        Glide.with(holder.itemView.getContext())
            .load(student.getImgStudent())
            .placeholder(R.drawable.loading)
            .error(R.drawable.error_image)
            .circleCrop()
            .into(holder.imvStudent);
        holder.tvAgeStudent.setText(calculateAge(studentList.get(position).getBirthDay()) + " tuổi");
        holder.tvNameStudent.setText(studentList.get(position).getName());
        holder.tvClassStudent.setText(studentList.get(position).getClassName());

        holder.btnDeleteStudent.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("students")
                    .document(studentList.get(position).getId()) // đảm bảo student có trường id
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                        studentList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, studentList.size());
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Xoá thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

        holder.btnDetailStudent.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailStudentActivity.class);
            intent.putExtra("ID",studentList.get(position).getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
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
