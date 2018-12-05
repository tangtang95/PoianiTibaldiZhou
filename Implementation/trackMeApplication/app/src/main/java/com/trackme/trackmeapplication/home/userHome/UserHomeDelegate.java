package com.trackme.trackmeapplication.home.userHome;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;

import butterknife.BindView;

public class UserHomeDelegate extends BaseActivityDelegate<
        UserHomeContract.UserHomeView,
        UserHomePresenter> implements
        NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolBar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;

    @Override
    public void onCreate(UserHomePresenter presenter) {
        super.onCreate(presenter);

        configureDrawer();
        configureToolbar();
    }

    private void configureDrawer() {
        View navHeader = navigationView.getHeaderView(0);
        TextView headerUsername = navHeader.findViewById(R.id.nav_header_user_name);
        TextView headerSSN = navHeader.findViewById(R.id.nav_header_user_sn);
        TextView circle = navHeader.findViewById(R.id.textViewCircle);

        String username = mPresenter.getView().getUsername();

        if (username != null && !username.isEmpty()) {
            headerUsername.setText(username);
            circle.setText(username.substring(0, 1));
            /*TODO*/
            headerSSN.setText("SSN:");
        }

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

    private void configureToolbar(){
        UserPageAdapter pageAdapter = new UserPageAdapter(mPresenter.getView().getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }


}
