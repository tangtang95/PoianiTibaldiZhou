package com.trackme.trackmeapplication.baseUtility;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.*;

public class BaseActivityTest {

    @Mock
    private BasePresenterImpl basePresenter;

    @Spy
    private BaseActivity baseActivitySpy;

    private BaseActivity baseActivity;

    @Before
    public void setUp(){
        baseActivity = new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return basePresenter;
            }
        };

        MockitoAnnotations.initMocks(this);
        baseActivitySpy = Mockito.spy(baseActivity);
    }

    @Test
    public void onCreate() {
        assertNotNull(baseActivitySpy);
        assertNull(baseActivity.getProgressBar());
    }

    @Test
    public void onDestroy() {
        assertNull(baseActivity.getmUnBinder());
    }

    @Test
    public void showProgress() {
        baseActivity.showProgress();
    }

    @Test
    public void hideProgress() {
        baseActivity.hideProgress();
    }

    @Test
    public void equals() {
        assertEquals(baseActivity, baseActivity);
        BaseActivity baseActivity2 = new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return new BasePresenterImpl();
            }
        };
        BaseActivity baseActivity3 = new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return basePresenter;
            }
        };
        assertNotEquals(baseActivity, baseActivity2);
        assertNotEquals(baseActivity, null);
        assertEquals(baseActivity.mPresenter, baseActivity3.mPresenter);
        assertTrue(baseActivity.equals(baseActivity3));
    }

    @Test
    public void hashcode(){
        assertEquals(baseActivity.hashCode(), baseActivity.hashCode());
        BaseActivity baseActivity2 = new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return new BasePresenterImpl();
            }
        };
        assertEquals(baseActivity.hashCode(), baseActivity2.hashCode());
    }

}