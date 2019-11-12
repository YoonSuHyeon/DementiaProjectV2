package com.example.last;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.StringTokenizer;

@RequiresApi(api = Build.VERSION_CODES.M)

public class HosmapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Intent intent;
    private LocationManager locationMgr;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    double x, y;
    String[] buffer;
    Location location;
    Button find, call;
    TextView t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosmap);
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_view);
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("tx7ojyc1re"));
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        find = findViewById(R.id.findbutton);
        call = findViewById(R.id.casllbutton);
        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);

        intent = getIntent();
        String str = intent.getStringExtra("list");

        StringTokenizer token = new StringTokenizer(str, "/");
        buffer = new String[token.countTokens()];
        int i = 0;
        while (token.hasMoreTokens()) {
            buffer[i] = token.nextToken();
            i++;
        }//0: 주소 1: 전화번호 2: y좌표 3: x좌표 4: 병원이름
        t1.setText("병원명:" + buffer[4]);
        t2.setText("주소" + buffer[0]);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ComponentName compName = new ComponentName("com.samsung.android.contacts","com.android.contacts.activities.CallLogActivity");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.map.naver.com/search2/search.nhn?query=" + buffer[4]));
                startActivity(intent);

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + buffer[1]));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        x = Double.parseDouble(buffer[3]);//x좌표
        y = Double.parseDouble(buffer[2]);//y좌표
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//정확도
        criteria.setPowerRequirement(Criteria.POWER_LOW);//낮은 전원소비량
        criteria.setAltitudeRequired(false);//고도, 높이 값을 얻어 올지를 결정
        criteria.setBearingRequired(false);//기본 정보
        criteria.setSpeedRequired(false);//속도
        criteria.setCostAllowed(true);//비용
        String bestProvider = locationMgr.getBestProvider(criteria, true);// 내 위치
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        location = locationMgr.getLastKnownLocation(bestProvider);
        mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(this);
        Log.d("TAG", "getStringExtra: "+str);
        Location locationA = new Location("A");
        locationA.setLatitude(location.getLatitude());//위도
        locationA.setLongitude(location.getLongitude());//경도
        Location locationB = new Location("B");
        locationB.setLatitude(x);//위도
        locationB.setLongitude(y);// 경도
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        LatLng coord = new LatLng(x,y);
        Log.d("TAG", "x: "+x);
        Log.d("TAG", "y: "+y);
        //mapView.onStart();
        Marker marker = new Marker();
        naverMap.moveCamera(CameraUpdate.scrollTo(coord));
        marker.setPosition(new LatLng(x, y));//buffer[2],buffer[3] 37.5670135  126.9783740
        marker.setMap(naverMap);
        marker.setCaptionText(buffer[4]);//병원이름 타이틀
        marker.setCaptionAlign(Align.Top);//타이틀 위치
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);//건물그룹
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true);//실시간교통량
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);//대중교통
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true);//산그룹(대표: 등고선, 산책로)
        naverMap.setIndoorEnabled(true);//건물 내부 실내지도
        naverMap.setMapType(NaverMap.MapType.Basic);//일반지도
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);//나침판 활성화
        uiSettings.setLogoClickEnabled(true);//네이버 로고 클릭을 활성화할지 여부를 지정합니다.
        uiSettings.setLocationButtonEnabled(true);

    }
}
