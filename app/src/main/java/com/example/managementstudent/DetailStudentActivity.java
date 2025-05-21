package com.example.managementstudent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.managementstudent.adapter.CertificateAdapter;
import com.example.managementstudent.model.Certificate;
import com.example.managementstudent.model.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailStudentActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Intent intent;

    EditText edtClassStudent, edtPosition, edtIndustry, edtFaculty, edtBirthDay;

    TextView tvNameStudentDetail;
    ImageView imvStudentDT, btnOpenCamera;
    Button btnSave, btnCancel, btnAddCertificate;
    String studentID;
    List<Certificate> certificateList;
    CertificateAdapter adapter;
    final Certificate DEFAULT_CERTIFICATE = new Certificate("CT000", "Chưa có chứng chỉ", "");
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    Student originalStudent;
//    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_student);

        init();

        getStudent();
        changeImage();
        btnSave.setOnClickListener(v -> {
            updateStudentData();
        });
        addCertificate();
    }

    private void updateStudentData() {
        // Lấy dữ liệu từ EditText
        String name = tvNameStudentDetail.getText().toString().trim();
        String className = edtClassStudent.getText().toString().trim();
        String birthDay = edtBirthDay.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String faculty = edtFaculty.getText().toString().trim();
        String industry = edtIndustry.getText().toString().trim();

        if (name.isEmpty() || className.isEmpty() || birthDay.isEmpty() || position.isEmpty() || faculty.isEmpty() || industry.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(birthDay)) {
            Toast.makeText(this, "Ngày sinh không hợp lệ. Định dạng dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả sử bạn có uri ảnh đang hiển thị
        String imgStudent = imvStudentDT.getTag() != null ? imvStudentDT.getTag().toString() : "";
        removeIfNotEmpty();
        // Tạo đối tượng Student
        Student student = new Student(
                studentID, name, className, birthDay,
                position, faculty, industry, imgStudent, certificateList
        );

        if (imageUri != null) {
            uploadImageToFirebase(imageUri, downloadUrl -> {
                saveStudentToFirestore(new Student(
                        studentID, name, className, birthDay,
                        position, faculty, industry, downloadUrl, certificateList
                ));
            });
        } else {
            saveStudentToFirestore(student);
        }

    }

    private void uploadImageToFirebase(Uri imageUri, OnImageUploadListener listener) {
        String fileName = "student_images/" + studentID + "_" + System.currentTimeMillis() + ".jpg";
        FirebaseStorage.getInstance().getReference(fileName)
                .putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        listener.onUploadSuccess(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveStudentToFirestore(Student student) {

        db.collection("students").document(studentID)
                .set(student)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private interface OnImageUploadListener {
        void onUploadSuccess(String downloadUrl);
    }

    public void init() {
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        studentID = intent.getStringExtra("ID");

        edtClassStudent = findViewById(R.id.edtClassStudent);
        edtPosition = findViewById(R.id.edtPosition);
        edtIndustry = findViewById(R.id.edtIndustry);
        edtFaculty = findViewById(R.id.edtFaculty);
        tvNameStudentDetail = findViewById(R.id.tvNameStudentDetail);
        imvStudentDT = findViewById(R.id.imvStudentDT);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        edtBirthDay = findViewById(R.id.edtBirthDay);



        btnSave = findViewById(R.id.btnSave);
        btnAddCertificate = findViewById(R.id.btnAddCertificate);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hasChanges()) {
                    showSaveDialog();
                } else {
                    finish(); // hoặc requireActivity().onBackPressedDispatcher.onBackPressed() nếu dùng Fragment
                }
            }
        });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.person)
                                .circleCrop()
                                .into(imvStudentDT);
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.person)
                                .centerCrop()
                                .circleCrop()
                                .into(imvStudentDT);
                        // <-- Gán URI để lưu được khi cập nhật
                    }
                });


    }

    public void getStudent() {
        db.collection("students") // tên collection
                .document(studentID)    // ID của document
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        originalStudent = student;
                        edtClassStudent.setText(student.getClassName());
                        edtPosition.setText(student.getPosition());
                        edtIndustry.setText(student.getIndustry());
                        edtFaculty.setText(student.getFaculty());
                        edtBirthDay.setText(student.getBirthDay());
                        tvNameStudentDetail.setText(student.getName());

                        Glide.with(this)
                                .load(student.getImgStudent())
                                .placeholder(R.drawable.person) // ảnh mặc định khi đang tải
                                .error(R.drawable.person)
                                .circleCrop()    // ảnh lỗi
                                .into(imvStudentDT);
                        certificateList = student.getCertificateList();
                        ListView lvCertificates = findViewById(R.id.lvCertificateStudent);
                        if (certificateList.isEmpty()) {
                            certificateList.add(DEFAULT_CERTIFICATE);
                            Log.d("DEBUG", "null");

                            adapter = new CertificateAdapter(this, certificateList);
                            lvCertificates.setAdapter(adapter);
                        } else {
                            adapter = new CertificateAdapter(this, certificateList);
                            lvCertificates.setAdapter(adapter);
                        }
                    } else {
                        // Document không tồn tại
                        Log.d("Firestore", "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting document", e);
                });
    }

    public void changeAvatar() {
        btnCancel.setOnClickListener(v -> {

        });
    }

    private void addCertificate() {
        btnAddCertificate.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_add_certificate, null);

            EditText edtCertificateId = view.findViewById(R.id.edtCertificateId);
            EditText edtCertificateName = view.findViewById(R.id.edtCertificateName);
            EditText edtCertificateDescription = view.findViewById(R.id.edtCertificateDescription);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm chứng chỉ")
                    .setView(view)
                    .setPositiveButton("Thêm", (dialog, which) -> {
                        String id = edtCertificateId.getText().toString().trim();
                        String name = edtCertificateName.getText().toString().trim();
                        String des = edtCertificateDescription.getText().toString().trim();

                        if (id.isEmpty() || name.isEmpty() || des.isEmpty()) {
                            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        removeIfNotEmpty();
                        Certificate certificate = new Certificate(id, name, des);
                        certificateList.add(certificate); // List bạn đã khai báo
                        adapter.notifyDataSetChanged(); // Nếu bạn dùng adapter
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void removeIfNotEmpty() {
        if (!certificateList.isEmpty()) {
            certificateList.removeIf(certificate -> "CT000".equals(certificate.getId()));
        }
    }

    public void changeImage() {
        btnOpenCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
            showImageOptions();
        });

    }

    private void showImageOptions() {
        String[] options = {"Chụp ảnh", "Chọn từ thiết bị"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else {
                openGallery();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
                imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraLauncher.launch(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private boolean isCertificateListSame() {
        if(certificateList.size() == originalStudent.getCertificateList().size())
            return false;
        for (int i = 0; i < certificateList.size(); i++) {
            Certificate c1 = certificateList.get(i);
            Certificate c2 = originalStudent.getCertificateList().get(i);
            if (!c1.getId().equals(c2.getId())){
                return false;
            }
        }
        return true;
    }

    private boolean hasChanges() {
        String currentClassName = edtClassStudent.getText().toString().trim();
        String currentBirthDay = edtBirthDay.getText().toString().trim();
        String currentPosition = edtPosition.getText().toString().trim();
        String currentFaculty = edtFaculty.getText().toString().trim();
        String currentIndustry = edtIndustry.getText().toString().trim();
        String currentImgUri = imageUri != null ? imageUri.toString() : originalStudent.getImgStudent();

        return !currentClassName.equals(originalStudent.getClassName()) ||
                !currentBirthDay.equals(originalStudent.getBirthDay()) ||
                !currentPosition.equals(originalStudent.getPosition()) ||
                !currentFaculty.equals(originalStudent.getFaculty()) ||
                !currentIndustry.equals(originalStudent.getIndustry()) ||
                !currentImgUri.equals(originalStudent.getImgStudent()) ||
                !isCertificateListSame();
    }

    private void showSaveDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Lưu thay đổi?")
                .setMessage("Bạn có muốn lưu trước khi thoát không?")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    updateStudentData();
                    finish();
                })
                .setNegativeButton("Không lưu", (dialog, which) -> finish())
                .setNeutralButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.menu_settings:
                Toast.makeText(this, "Mở cài đặt", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_logout:
                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                // TODO: Xử lý đăng xuất
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}