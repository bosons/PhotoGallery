package com.etuijian.android.photogallery2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by xyang on 4/23/15.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Eric", "CrimeActivity onCreate");
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            Log.i("Eric", "create new CrimeFragment");
        } else {  Log.i("Eric", "Find existing fragment");  }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Eric", "FragmentActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Eric", "FragmentActivity onResume");
    }
}
