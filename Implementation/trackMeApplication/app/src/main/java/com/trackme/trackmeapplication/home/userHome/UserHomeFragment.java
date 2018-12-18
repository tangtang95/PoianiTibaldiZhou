package com.trackme.trackmeapplication.home.userHome;

import android.arch.persistence.room.Room;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * UserHome fragment is the first fragment load after the login, that shows to the user the possibility
 * of seeing the last value register in the system of its health.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class UserHomeFragment extends BaseFragment {

    private static final int ANIMATION_DURATION = 2000;

    @BindView(R.id.textViewPulseValue)
    protected TextView pulseValue;
    @BindView(R.id.textViewBloodValue)
    protected TextView bloodPressureValue;
    @BindView(R.id.textViewPulseText)
    protected TextView pulseText;
    @BindView(R.id.textViewPressureText)
    protected TextView bloodPressureText;
    @BindView(R.id.imageViewHeart)
    protected ImageView image;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_user_home;
    }

    @Override
    protected void setUpFragment() {

    }

    @OnClick(R.id.home_check_button)
    public void onCheckYourStatusButtonClick() {
        handleAnimation();

        Runnable getLastData = () -> {
            AppDatabase appDatabase = Room.databaseBuilder(getmContext(),
                    AppDatabase.class, getmContext().getString(R.string.persistent_database_name)).build();

            HealthData healthData = appDatabase.getHealthDataDao().getLast();

            pulseValue.setText(healthData.getHeartbeat());
            String pressure = healthData.getPressureMax() + "/" + healthData.getPressureMin();
            bloodPressureValue.setText(pressure);
        };
        getLastData.run();
    }


    /**
     * Handle the image animation in the view.
     */
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
