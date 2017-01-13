package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Looper u;
	Joystick Jleft, Jright;
	
    public void robotInit() {
    	
    	Jleft = new Joystick(1);
    	Jright = new Joystick(0);
    	
    	u = new Looper(10);
    	//u.add(subsystem);
    	u.start();
    }
    
    public void autonomousInit() {
    	
    }
    
    public void teleopPeriodic() {
    	
    }
    
    public void disabledInit() {
    
    }  
}