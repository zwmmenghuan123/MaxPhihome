package com.phicomm.phihome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.phicomm.phihome.fragment.DeviceFragment;
import com.phicomm.phihome.fragment.MyFragment;
import com.phicomm.phihome.fragment.SceneFragment;

/**
 * Created by qisheng.lv on 2017/4/12.
 */
public class MainVpAdapter extends FragmentPagerAdapter {

    public MainVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new DeviceFragment();
        } else if (position == 1) {
            return new SceneFragment();
        } else {
            return new MyFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
