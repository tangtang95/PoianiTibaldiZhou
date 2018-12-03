package com.trackme.trackmeapplication.baseUtility;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasePresenterImplTest {

    private BasePresenterImpl basePresenter;

    @Before
    public void setUp(){
        basePresenter = new BasePresenterImpl();
    }

    @Test
    public void attachView() {
        BaseActivity baseActivity = new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return basePresenter;
            }
        };
        basePresenter.attachView(baseActivity);
        assertNotNull(basePresenter.getView());
        assertEquals(baseActivity, basePresenter.getView());
    }

    @Test
    public void detachView() {
        basePresenter.detachView();
        assertNull(basePresenter.getView());
    }

    @Test
    public void getView() {
        assertEquals(basePresenter.mView, basePresenter.getView());
    }

    @Test
    public void equals() {
        assertEquals(basePresenter, basePresenter);
        assertNotEquals(basePresenter, null);
        BasePresenterImpl basePresenter2 = new BasePresenterImpl();
        assertEquals(basePresenter2, basePresenter);
        BasePresenterImpl basePresenter3 = new BasePresenterImpl();
        basePresenter3.attachView(new BaseActivity() {
            @NonNull
            @Override
            protected BasePresenter getPresenterInstance() {
                return basePresenter;
            }
        });
        assertNotEquals(basePresenter, basePresenter3);
    }

    @Test
    public void hashcode(){
        BasePresenterImpl basePresenter2 = new BasePresenterImpl();
        assertEquals(basePresenter.hashCode(),basePresenter2.hashCode());
    }
}