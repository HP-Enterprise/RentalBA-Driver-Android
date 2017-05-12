package com.gjmgr.activity.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

	private Fragment[] fragment;
	
	public MyFragmentPagerAdapter(FragmentManager fm, Fragment[] fragment) {
		super(fm);
		this.fragment = fragment;
		
	}

	@Override
	public Fragment getItem(int i) {
		// TODO Auto-generated method stub
		return fragment[i];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragment.length;
	}

}
