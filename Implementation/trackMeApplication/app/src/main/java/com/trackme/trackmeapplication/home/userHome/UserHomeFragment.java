package com.trackme.trackmeapplication.home.userHome;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.home.UserLocationListener;

import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserHomeFragment extends Fragment {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int INITIAL_REQUEST = 1000;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 1;

    private static final int MAX_PULSE = 120;
    private static final int MIN_PULSE = 40;

    private static final int MAX_PRESSURE_X = 100;
    private static final int MIN_PRESSURE_X = 40;

    private static final int MAX_PRESSURE_Y = 190;
    private static final int MIN_PRESSURE_Y = 70;

    private static final int ANIMATION_DURATION = 3000;

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;

    // Define a listener that responds to location updates
    LocationListener locationListener = new UserLocationListener();

    View userHomeFragment;
    @BindView(R.id.editTextPulseValue)
    protected EditText pulseValue;
    @BindView(R.id.editTextBloodValue)
    protected EditText bloodPressureValue;
    @BindView(R.id.editTextPulseText)
    protected EditText pulseText;
    @BindView(R.id.editTextPressureText)
    protected EditText bloodPressureText;
    @BindView(R.id.imageViewHeart)
    protected ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!canAccessLocation())
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        setUpGPS();

        userHomeFragment = inflater.inflate(R.layout.fragment_user_home, container, false);
        ButterKnife.bind(this, userHomeFragment);
        return userHomeFragment;
    }

    @OnClick(R.id.home_check_button)
    public void onCheckYourStatusButtonClick() {
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        handleAnimation();

        Random random = new Random();
        String text1 = String.valueOf(random.nextInt(MAX_PULSE - MIN_PULSE + 1) + MIN_PULSE) + " bpm";
        String text2 =
                String.valueOf(random.nextInt(MAX_PRESSURE_Y - MIN_PRESSURE_Y + 1) + MIN_PRESSURE_Y) +
                        "/" +
                        String.valueOf(random.nextInt(MAX_PRESSURE_X - MIN_PRESSURE_X + 1) + MIN_PRESSURE_X);
        /*TODO*/
        pulseValue.setText(text1);
        bloodPressureValue.setText(text2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (canAccessLocation())
                    setUpGPS();
                break;
        }
    }

    private boolean canAccessLocation() {
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void setUpGPS() {
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }

    private void handleAnimation() {
        bloodPressureText.setAlpha(0f);
        pulseText.setAlpha(0f);
        bloodPressureValue.setAlpha(0f);
        pulseValue.setAlpha(0f);
        image.setAlpha(1f);

        image.animate().alpha(0f).setDuration(ANIMATION_DURATION);
        bloodPressureText.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        pulseText.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        bloodPressureValue.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        pulseValue.animate().alpha(1f).setDuration(ANIMATION_DURATION);
    }

}
