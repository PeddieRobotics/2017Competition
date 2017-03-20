package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Checkable;
import org.usfirst.frc.team5895.robot.lib.NavX;
import org.usfirst.frc.team5895.robot.lib.PID;
import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain2 {
	private Talon Mleft;
	private Talon Mright;
	double Lspeed, Rspeed;
	private enum Mode_Type {TELEOP, AUTO_RED_BALLS, AUTO_BLUE_BALLS, AUTO_RED_GEAR, AUTO_BLUE_GEAR, TURN_TO, DRIVE_TO};
	private Mode_Type mode = Mode_Type.TELEOP;
	private Encoder Eleft, Eright;
	private NavX NavX;
	private TrajectoryDriveController c_balls_red;
	private TrajectoryDriveController c_balls_blue;
	private TrajectoryDriveController cRLeft;
	private TrajectoryDriveController cMid;
	private TrajectoryDriveController cRRight;
	private TrajectoryDriveController cBLeft;
	private TrajectoryDriveController cBRight;
	private boolean reverseFrontBack = false;
	
	private String place;
	
	private PID distancePID;
	private double distance_kP = 0.04;
	private double distance_kI = 0;
	private double distance_kD = 0;
	private double distance_dV = 1;
	
	private PID turnPID;
	private double turn_kP = 0.02;
	private double turn_kI = 0;
	private double turn_kD = 0;
	private double turn_dV = 1;
	
	public DriveTrain2()
	{
		NavX=new NavX();
		
		Mleft = new Talon(ElectricalLayout.DRIVE_LEFTMOTOR);
		Mright = new Talon(ElectricalLayout.DRIVE_RIGHTMOTOR);

		Eleft = new Encoder(ElectricalLayout.DRIVE_LEFTENCODER,ElectricalLayout.DRIVE_LEFTENCODER2, true, Encoder.EncodingType.k4X);
		Eright = new Encoder(ElectricalLayout.DRIVE_RIGHTENCODER,ElectricalLayout.DRIVE_RIGHTENCODER2, false, Encoder.EncodingType.k4X);

		Eleft.setDistancePerPulse(4/12.0*3.14/360);
		Eright.setDistancePerPulse(4/12.0*3.14/360);
		
		distancePID = new PID(distance_kP, distance_kI, distance_kD, distance_dV);
		turnPID = new PID(turn_kP, turn_kI, turn_kD, turn_dV);

		try {
			c_balls_red = new TrajectoryDriveController("/home/lvuser/AutoFiles/Shoot/Balls_Red.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
		//	c_balls_blue = new TrajectoryDriveController("/home/lvuser/AutoFiles/Shoot/Balls_Blue.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, 0.008);
			c_balls_blue = new TrajectoryDriveController("/home/lvuser/AutoFiles/Shoot/Straight.txt", 0.0, 0, 0, 1.0/13.0, 1.0/50.0, 0.00);
			cRLeft = new TrajectoryDriveController("/home/lvuser/AutoFiles/Gear/RedLeft.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
			cMid = new TrajectoryDriveController("/home/lvuser/AutoFiles/Gear/middlePath.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
			cRRight = new TrajectoryDriveController("/home/lvuser/AutoFiles/Gear/RedRight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
			cBLeft = new TrajectoryDriveController("/home/lvuser/AutoFiles/Gear/BlueLeft.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
			cBRight = new TrajectoryDriveController("/home/lvuser/AutoFiles/Gear/BlueRight.txt", 0.2, 0, 0, 1.0/13.0, 1.0/50.0, -0.010);
		} catch (Exception e){
			DriverStation.reportError("Auto files not on robot!", false);
		}
	}

	/**
	 * Returns the average distance the drivetrain has driven
	 * 
	 * @return The distance in feet
	 */
	public double getDistance() {

		double distance = (Eleft.getDistance() + Eright.getDistance())/2;

		return distance;
	}

	/**
	 * Returns the speed of the drivetrain
	 * 
	 * @return The speed in feet per second
	 */
	public double getSpeed() {
		return (Eleft.getRate() + Eright.getRate())/2;
	}

	/**
	 * Returns the angle of the robot
	 * 
	 * @return The angle of the robot in degrees
	 */
	public double getAngle(){
		double angle = NavX.getAngle();

		return angle;
	}
	
	/**
	 * Resets encoders and NavX
	 */
	public void resetEncodersAndNavX(){
		Eleft.reset();
		Eright.reset();
		NavX.reset();
	}
	
	/**
	 * Follows a path autonomously (red alliance)
	 */
	public void auto_balls_redDrive() {
		resetEncodersAndNavX();
		c_balls_red.reset();
		mode = Mode_Type.AUTO_RED_BALLS;
	}
	
	/**
	 * Follows a path autonomously (blue alliance)
	 */
	
	public void resetGearSplinesRed(){
		cRLeft.reset();
		cMid.reset();
		cRRight.reset();
	}
	
	
	public void resetGearSplinesBlue(){
		cBLeft.reset();
		cMid.reset();
		cRRight.reset();
	}
	
	public void auto_balls_blueDrive() {
		resetEncodersAndNavX();
		c_balls_blue.reset();
		mode = Mode_Type.AUTO_BLUE_BALLS;
	}
	
	public void auto_gears_redDrive(){
		resetEncodersAndNavX();
		driveTo(-95);
//		resetGearSplinesRed();
//		mode = Mode_Type.AUTO_RED_GEAR;
	}
	
	public void auto_gears_blueDrive(){
		resetEncodersAndNavX();
		driveTo(-95);
//		resetGearSplinesBlue();
//		mode = Mode_Type.AUTO_BLUE_GEAR;
	}
	
	public void setSide(String side){
		place=side;
	}
	
	public boolean trajectoryFinished(){
		if(c_balls_blue.onTarget() || c_balls_red.onTarget()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * drives to a certain distance
	 * @param distance The distance to go
	 */
	public void driveTo(double distance) {
		distancePID.set(distance);
		mode = Mode_Type.DRIVE_TO;
	}
	/**
	 * turns to an angle
	 * @param angle The desired angle
	 */
	public void turnTo(double angle) {
		turnPID.set(angle);
		mode = Mode_Type.TURN_TO;
	}
	/**
	 * Two-controller driving
	 * 
	 * @param speed The forward/backwards motion
	 * @param turn The left/right turning motion
	 */
	public void arcadeDrive( double speed, double turn) {
		Lspeed = speed + turn;
		Rspeed = -speed + turn;
		mode = Mode_Type.TELEOP;
	}
	
	/**
	 * Sets speed of both sides
	 * 
	 * @param l The speed of the left side
	 * @param r The speed of the right side
	 */
	public void setLeftRightPower(double l, double r) {
		Lspeed = l;
		Rspeed = r;
		mode = Mode_Type.TELEOP;
	}
	
	public void setGearFront() {
		reverseFrontBack = true;
		
	}
	
	public void setIntakeFront() {
		reverseFrontBack = false;
	}
	
	/**
	 * Checks if we are within 1% of the distance we wanted
	 * @param distance
	 * @return
	 */
	public boolean atDistance(){
		if(getDistance() <= 1.01*distancePID.getSetpoint() && getDistance() >= .99*distancePID.getSetpoint()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Checks if we are within 1% of the angle we wanted
	 * @param angle
	 * @return
	 */
	public boolean atAngle(){
		if(getAngle() <= 1.01*turnPID.getSetpoint() && getAngle() >= .99*turnPID.getSetpoint()){
			return true;
		}else{
			return false;
		}
	}
	
	
	public void update()
	{

		switch(mode) {
		case AUTO_RED_BALLS:

			double[] m_red = new double[2];
			m_red = c_balls_red.getOutput(Eleft.getDistance(), Eright.getDistance(), -getAngle()*3.14/180);
			//DriverStation.reportError("the angle is" + getAngle(), false);

			Mleft.set(-m_red[0]);
			Mright.set(m_red[1]);
			break;
		
		case AUTO_BLUE_BALLS:
			
			double[] m_blue = new double[2];

			m_blue = c_balls_blue.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
			
			DriverStation.reportError(""+m_blue[0], false);

			Mleft.set(-m_blue[0]);
			Mright.set(m_blue[1]);
			break;
		
		case AUTO_BLUE_GEAR:
			
			double[] g_blue = new double[2];
			
			switch(place){
			case "left":
				g_blue = cBLeft.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			case "middle":
				g_blue = cMid.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			case "right":
				g_blue = cBRight.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			}
			
			Mleft.set(-g_blue[0]);
			Mright.set(-g_blue[1]);
			break;
			
		case AUTO_RED_GEAR:
			
			double[] g_red = new double[2];
			
			switch(place){
			case "left":
				g_red = cRLeft.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			case "middle":
				g_red = cMid.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			case "right":
				g_red = cRRight.getOutput(Eleft.getDistance(), Eright.getDistance(), getAngle()*3.14/180);
				break;
			}
			
			Mleft.set(-g_red[0]);
			Mright.set(g_red[1]);
			break;

		case TELEOP:

			if(reverseFrontBack) {
			Mleft.set(-Lspeed);
			Mright.set(-Rspeed);
			}
			else {
			Mleft.set(Lspeed);
			Mright.set(Rspeed);
			}

			break;
		case DRIVE_TO:
			
			double distanceOutput = distancePID.getOutput(getDistance());
			Mleft.set(-distanceOutput);
			Mright.set(distanceOutput);
			DriverStation.reportError("Distance is" + getDistance(), false);
			
			break;
		case TURN_TO:
			
			double turnOutput = turnPID.getOutput(getAngle());
			Mleft.set(-turnOutput);
			Mright.set(turnOutput);
			DriverStation.reportError("Angle is " + getAngle(), false);
			break;
		}
	}
}