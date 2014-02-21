package com.college.civilengineeringtools;

import java.util.ArrayList;

import android.widget.ImageView;

public class PointLoadManager extends PointLoad {
	
	private ArrayList<PointLoad> pLoads = new ArrayList<PointLoad>();
	
	private static PointLoadManager instance = null;
	
	public static PointLoadManager getInstance() {
		if(instance == null) {
			instance = new PointLoadManager();
		}
		return instance;
	}
	
	protected PointLoadManager() {
	
	}

	public void addPtLoad(PointLoad pl, ImageView image, CustomParams params) {
		// TODO Auto-generated method stub
		MainActivity.rlpl.addView(image, pLoads.size(), params);
		pLoads.add(pl);
		pl.setIndex(PointLoadManager.getInstance().getPtLoads().size() - 1);
	}
	
	public void deletePtLoad(int pos) {
		pLoads.remove(pos);
		MainActivity.rlpl.removeViewAt(pos);
		
		for(int i = 0; i < pLoads.size(); i++)
			pLoads.get(i).setIndex(i);
	}

	public ArrayList<PointLoad> getPtLoads() {
		return pLoads;
	}

	public void setPtLoads(ArrayList<PointLoad> pLoads) {
		this.pLoads = pLoads;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return pLoads.size();
	}

}
