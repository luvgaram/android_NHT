package org.nhnnext.nearhoneytip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.nhnnext.nearhoneytip.item.User;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;


public class LoginActivity extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private SharedPreferences pref;
    private RemoteService remoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermission();
    }

    private void logInOrSignUp() {
        final String uuid = getDevicesUUID();

        // check uuid availability
        if (uuid != null) {
            Log.d("login", uuid);

            // check existence of uuid in SharedPreferences
            pref = getSharedPreferences("login", MODE_PRIVATE);
            editSharedPreferences(uuid);

            Intent intent = new Intent(this, MainActivity.class);
            // TODO 서버 코드 여기다!
//            SystemClock.sleep(3000);
            startActivity(intent);

        } else Toast.makeText(this, "사용할 수 없는 기기입니다", Toast.LENGTH_LONG).show();
    }

    private void editSharedPreferences(final String uuid) {

        remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);

        TypedString newUser = new TypedString("{" + "\"uid\":" + "\"" + uuid + "\"" + "}");
        remoteService.postUser(newUser, new Callback<User>() {

            @Override
            public void success(User user, Response response) {
                Log.d("retrofit", "test success");
                putUserInPref(user);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("retrofit", "test failure");
            }
        });
    }

    private void putUserInPref(User user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("uuid", user.get_id());
        editor.putString("nickname", user.getNickname());
        editor.putString("profilephoto", user.getProfilephoto());

        editor.commit();

        Log.d("pref", pref.getString("nickname", ""));
    }

    private String getDevicesUUID() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "앱 실행을 위해서는 전화 관리 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {
            logInOrSignUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    logInOrSignUp();

                } else {
                    Toast.makeText(this, "앱 실행을 위해서는 전화 관리 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
