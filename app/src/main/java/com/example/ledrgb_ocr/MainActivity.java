package com.example.ledrgb_ocr;

//import các thư viện
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    // Lưu và đọc dữ liệu từ Realtime DB thông qua biến "LED" trên firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("LED");

    //khai báo các biến
    Button chooseImage,manual;
    ImageView image;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ánh xạ
        manual = findViewById(R.id.manual);
        chooseImage = findViewById(R.id.button);
        image = findViewById(R.id.imageView);
        text = findViewById(R.id.textView);

        //đặt sự kiện cho nút nhấn "manual"
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, manualActivity.class);
                startActivity(intent);
            }
        });

        //đặt sự kiện cho nút nhấn "chooseImage"
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChooseImage();
            }});
    }



    public void setChooseImage(){
        // Tạo một Intent không tường minh,
        // yêu cầu hệ thống mở Camera chuẩn bị chụp hình
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start Activity chụp hình, và chờ đợi kết quả trả về.
        this.startActivityForResult(intent, 1);
    }

    //nếu chụp ảnh và ảnh trả về Ok thì gọi chạy hàm nhận dạng ký tự bằng hàm runTextRecognition
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bp = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                this.image.setImageBitmap(bp);
                runTextRecognition(bp);
            }
        }
    }


    //sử dụng công cụ thị giác firebase để xử lý hình ảnh
    private void runTextRecognition(Bitmap mSelectedImage) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
        FirebaseVisionTextRecognizer detector =
                FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(
                new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText texts) {
                        processTextRecognitionResult(texts);
                    }
                });
    }



    //hàm xử lý nhận dạng ký tự và trả về chuỗi ký tự đã nhận dạng
    @SuppressLint("SetTextI18n")
    private void processTextRecognitionResult(FirebaseVisionText texts) {
        FirebaseApp.initializeApp(MainActivity.this);
            List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            text.setText("No text found!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++)
                    sb.append(elements.get(k).getText());
            }

        }

        //hiển thị chuỗi đã nhận dạng ra TextView
        text.setText(sb.toString());


//Xử lý lại chuỗi text bằng cách cách bỏ khoảng trắng hai đầu, in thường tất cả, sau đó gán vào biến ans
        String ans = ((TextView)text).getText().toString().trim().toLowerCase();


//kiểm tra nội dung biến ans này với các ký tự điều khiển để gởi giá trị tương ứng lên biến LED trên firebase
        switch (ans) {
            case "red":
                myref.setValue("000");
                break;
            case "blue":
                myref.setValue("001");
                break;
            case "green":
                myref.setValue("010");
                break;
            case "yellow":
                myref.setValue("011");
                break;
            case "cyan":
                myref.setValue("100");
                break;
            case "purple":
                myref.setValue("101");
                break;
            case "white":
                myref.setValue("110");
                break;
            case "off":
                myref.setValue("111");
                break;
            case "blink":
                myref.setValue("0000");
                break;
        }
    }
}