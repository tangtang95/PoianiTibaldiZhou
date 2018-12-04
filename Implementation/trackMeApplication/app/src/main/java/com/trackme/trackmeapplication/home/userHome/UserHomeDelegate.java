package com.trackme.trackmeapplication.home.userHome;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;

import butterknife.BindView;

public class UserHomeDelegate extends BaseActivityDelegate<
        UserHomeContract.UserHomeView,
        UserHomePresenter> implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolBar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @Override
    public void onCreate(UserHomePresenter presenter) {
        super.onCreate(presenter);
        configureDrawer();
    }

    private void configureDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mPresenter.getView().getActivity(),
                drawerLayout,
                toolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                mPresenter.onMessageSelected();
                break;
            case R.id.nav_profile:
                mPresenter.onProfileSelected();
                break;
            case R.id.nav_settings:
                mPresenter.onSettingsSelected();
                break;
        }
        closeDrawer();
        return true;
    }

    public boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    public void setCheckedItem(int r) {
        navigationView.setCheckedItem(r);
    }

}
