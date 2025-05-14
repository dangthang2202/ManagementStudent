package com.example.managementstudent.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managementstudent.R;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    public ImageView imvStudent;
    public TextView tvNameStudent, tvAgeStudent, tvClassStudent;

    public Button btnDeleteStudent, btnEditStudent, btnDetailStudent;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        imvStudent = itemView.findViewById(R.id.imgStudent);

        tvAgeStudent = itemView.findViewById(R.id.tvAgeStudent);
        tvNameStudent = itemView.findViewById(R.id.tvNameStudent);
        tvClassStudent = itemView.findViewById(R.id.tvClassStudent);

        btnDeleteStudent = itemView.findViewById(R.id.btnDeleteStudent);
        btnEditStudent = itemView.findViewById(R.id.btnEditStudent);
        btnDetailStudent = itemView.findViewById(R.id.btnDetailStudent);
    }


}
