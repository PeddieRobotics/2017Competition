package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class GearReceiver {
	
	private Solenoid myCylinder;
	private boolean isDown;
	
	public GearReceiver() {
		myCylinder = new Solenoid(0);
		isDown = false;
		
	}
	
	public void up() {
		isDown = false;
	}
	
	public void down() {
		isDown = true;
		
	}
	
	public void update() {
		
		if(myCylinder.get() != isDown)
			myCylinder.set(isDown);
		
	}

}