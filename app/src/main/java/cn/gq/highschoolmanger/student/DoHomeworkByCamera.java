package cn.gq.highschoolmanger.student;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.gq.highschoolmanger.R;
import cn.gq.highschoolmanger.doRequest.BaseRequest;
import cn.gq.highschoolmanger.entity.SendHomeWork;
import cn.gq.highschoolmanger.loading.LoadingX;
import cn.gq.highschoolmanger.utils.BitMapUriFileUtils;

public class DoHomeworkByCamera extends AppCompatActivity implements View.OnClickListener {
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int CROP_PHOTO = 2;
    public static final int LOCAL_PHOTO = 3;
    private Button takePhoto;
    private Button localPhoto;
    private TextView bt_send_to_server ;
    private ImageView picture;
    private Uri imageUri;
    private Bitmap bitmap ;
    private SendHomeWork sendHomeWork ;
    public static File tempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
                  
        if(intent.getSerializableExtra("homework")!=null) {
            sendHomeWork = (SendHomeWork) intent.getSerializableExtra("homework");
        }
        setContentView(R.layout.camera_mian);
        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        localPhoto = findViewById(R.id.open_local) ;
        bt_send_to_server = findViewById(R.id.zuoye_btn_add) ;
        bt_send_to_server.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        localPhoto.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                openCamera(this);
                break;
            case R.id.open_local :
                openGallery();
                break;
            case R.id.zuoye_btn_add :
                saveHomework();
                break;
        }
    }

    private void saveHomework() {

        //           todo ByteArrayOutputStream pictureOut = new ByteArrayOutputStream();
//           todo this.bitmap.compress(Bitmap.CompressFormat.PNG,100,pictureOut);
//           todo pictureOut.close();
//           todo byte[] bytes = pictureOut.toByteArray();

        // String s = getContentResolver().openOutputStream(imageUri).toString();
        // System.out.println("out:"+s);
        // System.out.println("图片大小" +bytes.length);

        // todo  String photo = Base64.encodeToString(bytes,0,bytes.length,Base64.DEFAULT);
        File test_temp = BitMapUriFileUtils.saveBitmapFile(this.bitmap, "test_temp");
        // System.out.println(photo);
        // todo  sendToServer(photo);
        setToServe(test_temp);

    }

    public void  setToServe (File homeworkPic) {
        LoadingX loadingX = new LoadingX(this);
        loadingX.setTitle("作业提交");
        loadingX.setMessage("...");
        RequestParams params = new RequestParams();

            try {
                params.put("studentFile", homeworkPic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            params.put("tshId", sendHomeWork.getId());
            BaseRequest baseRequest = new BaseRequest(loadingX) {
                @Override
                public void onFailureDo(int code) {

                }

                @Override
                public void onSuccessDo(JSONObject data) {

                }
            };
            baseRequest.doPost(null,params,"student/submitHomework_1");
        }

   public void sendToServer(String photo) {
       LoadingX loadingX = new LoadingX(this);
       loadingX.setTitle("作业提交");
       loadingX.setMessage("...");
       RequestParams params = new RequestParams();
       params.put("tshId",sendHomeWork.getId());
       params.put("homeworkPicture",photo);
       BaseRequest baseRequest = new BaseRequest(loadingX) {
           @Override
           public void onFailureDo(int code) {

           }

           @Override
           public void onSuccessDo(JSONObject data) {

           }
       };

       baseRequest.doPost(params,"student/submitHomework");
   }

    public void openCamera(Activity activity) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //检查是否有存储权限，以免崩溃
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                if (resultCode == RESULT_OK) {
                    // String uri_path = getFilePathByUri(this, data.getData());
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        // 将得到的BitMap进行编辑
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
              case  LOCAL_PHOTO :
                if (resultCode == RESULT_OK) {
                    if(data != null) {
                        Uri uri = data.getData();
                        imageUri = uri;
                    }
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,LOCAL_PHOTO);
    }

}
