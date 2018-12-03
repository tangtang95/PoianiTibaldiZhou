package com.trackme.trackmeapplication.baseUtility;


import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BaseActivityDelegateTest {

    @Mock
    private BasePresenterImpl basePresenter;

    private BaseActivityDelegate baseActivityDelegate;

    @Before
    public void setUp() {
        baseActivityDelegate = new BaseActivityDelegate() {
            @Override
            public void onCreate(BasePresenterImpl presenter) {
                super.onCreate(presenter);
            }
        };
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onCreate() {
        when(basePresenter.getView()).thenReturn(Mockito.mock(BaseView.class));
        baseActivityDelegate.onCreate(basePresenter);
        assertEquals(baseActivityDelegate.mPresenter, basePresenter);
        assertNotNull(baseActivityDelegate.getmUnBinder());
    }

    @Test
    public void onDestroy() {
        when(basePresenter.getView()).thenReturn(Mockito.mock(BaseView.class));
        baseActivityDelegate.onCreate(basePresenter);
        baseActivityDelegate.onDestroy();
        assertNotNull(baseActivityDelegate.getmUnBinder());
    }
}