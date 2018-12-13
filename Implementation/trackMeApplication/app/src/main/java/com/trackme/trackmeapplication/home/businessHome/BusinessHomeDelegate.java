package com.trackme.trackmeapplication.home.businessHome;

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
import com.trackme.trackmeapplication.home.userHome.UserPageAdapter;

import butterknife.BindView;

public class BusinessHomeDelegate extends BaseActivityDelegate<
        BusinessHomeContract.BusinessHomeView,
        BusinessHomePresenter> implements
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
    public void onCreate(BusinessHomePresenter presenter) {
        super.onCreate(presenter);

        configureDrawer();
    }

    private void configureDrawer() {
        View navHeader = navigationView.getHeaderView(0);
        TextView headerUsername = navHeader.findViewById(R.id.nav_header_mail);
        TextView circle = navHeader.findViewById(R.id.textViewCircle);

        String mail = mPresenter.getView().getMail();

        if (mail != null && !mail.isEmpty()) {
            headerUsername.setText(mail);
            circle.setText(mail.substring(0, 1));
            /*TODO*/
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
            case R.id.nav_logout:
                mPresenter.onLogoutSelected();
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

    public void configureToolbar() {
        BusinessPageAdapter pageAdapter = new BusinessPageAdapter(mPresenter.getView().getActivity().getSupportFragmentManager(),
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
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
