package com.example.managementstudent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.managementstudent.R;
import com.example.managementstudent.model.Certificate;

import java.util.List;

public class CertificateAdapter extends ArrayAdapter<Certificate> {
    private Context context;
    private List<Certificate> certificateList;
    final Certificate DEFAULT_CERTIFICATE = new Certificate("CT000", "Chưa có chứng chỉ", "");

    public CertificateAdapter(Context context, List<Certificate> certificateList) {
        super(context, 0, certificateList);
        this.context = context;
        this.certificateList = certificateList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Certificate certificate = certificateList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.certificate_student_view, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvCertificateShow);

        tvName.setText(certificate.getName());
        tvName.setSelected(true);
        if(!certificate.getId().equals("CT000")){
            Log.d("DEBUG",certificate.getId());
            tvName.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa chứng chỉ")
                        .setMessage("Bạn có chắc muốn xóa chứng chỉ này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            certificateList.remove(position);
                            if(certificateList.isEmpty()){
                                certificateList.add(DEFAULT_CERTIFICATE);
                            }
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
        }


        return convertView;
    }
}
