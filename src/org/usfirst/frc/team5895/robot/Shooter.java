package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc.team5895.robot.lib.PID;
import org.usfirst.frc.team5895.robot.lib.TrajectoryDriveController;

public class Shooter {

	private Talon flywheelMotor;
	private Talon conveyorMotor;
	private Solenoid hood;
	private double speed;

	PID PID;
	Counter Counter;

	double Kp = 0.24;
	double Ki = 0.000018;
	double Kd = 0.00000005;
	double dV = 1;


	public Shooter()
	{
		flywheelMotor = new Talon(ElectricalLayout.FLYWHEEL_MOTOR);
		conveyorMotor = new Talon(ElectricalLayout.CONVEYOR_MOTOR);
		hood = new Solenoid(ElectricalLayout.FLYWHEEL_SOLENOID);

		PID = new PID(Kp, Ki, Kd, dV, false);
		Counter = new Counter(ElectricalLayout.FLYWHEEL_COUNTER);
		Counter.setDistancePerPulse(1);
		
	}
	
	/**
	 * Shoot continously
	 */
	public void shoot(){
		speed = 0.6;
	}
	
	/**
	 * Stop shooting
	 */
	public void stopShoot() {
		speed = 0;
	}
	
	/**
	 * Conveyor goes in reverse
	 */
	public void reverse() {
		speed = -0.6;
	}
	
	/**
	 * Return the speed of the fly wheel in RPM
	 * @return The speed, in RPM, of the fly wheel
	 */
	public double getSpeed() {
		double flywheelSpeed = Counter.getRate()*60;
		return flywheelSpeed;
	}
	

	/**
	 * Sets the target RPM of the flywheel
	 * @param Aetpoint angular speed set in RPM
	 */
	public void setSpeed(double setpoint) {
		PID.set(setpoint/60);
	}
	
	/**
	 * Tells whether flywheel speed is close to the setpoint
	 * @return Whether it's close or not
	 */
	
	public boolean atSpeed()
	{
		return ( getSpeed() < PID.getSetpoint() + 20 && getSpeed() > PID.getSetpoint() - 20 );	
	}
	
	public void update() {
		flywheelMotor.set(PID.getOutput(Counter.getRate()));
		conveyorMotor.set(speed);
	}
}

