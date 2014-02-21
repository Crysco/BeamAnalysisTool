package com.college.civilengineeringtools;

import java.util.ArrayList;

import android.widget.ImageView;

public class DistributedLoadManager extends DistributedLoad {
	
	private ArrayList<DistributedLoad> dLoads = new ArrayList<DistributedLoad>();
	
	private static DistributedLoadManager instance = null;
	
	public static DistributedLoadManager getInstance() {
		if(instance == null) {
			instance = new DistributedLoadManager();
		}
		return instance;
	}
	
	protected DistributedLoadManager() {
	}

	public void addDistLoad(DistributedLoad dl, ImageView[] images, CustomParams[] params) {
		// TODO Auto-generated method stub

		MainActivity.rldl.addView(images[0], 2 * dLoads.size(), params[0]);
		MainActivity.rldl.addView(images[1], 2 * dLoads.size() + 1, params[1]);
		
		dLoads.add(dl);
		
		dl.setIndex(DistributedLoadManager.getInstance().getDistLoads()
				.size() - 1);
		
		MainActivity.distLoadLine.draw(MainActivity.distLoadCanvas);
	}
	
	public void deleteDistLoad(int pos) {
		dLoads.remove(pos);
		MainActivity.rldl.removeViews(2 * pos, 2);
		
		for(int i = 0; i < dLoads.size(); i++)
			dLoads.get(i).setIndex(i);
		
		MainActivity.distLoadLine.draw(MainActivity.distLoadCanvas);
	}
	
	public ArrayList<DistributedLoad> getDistLoads() {
		return dLoads;
	}

	public void setDistLoads(ArrayList<DistributedLoad> distLoads) {
		this.dLoads = distLoads;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return dLoads.size();
	}

}