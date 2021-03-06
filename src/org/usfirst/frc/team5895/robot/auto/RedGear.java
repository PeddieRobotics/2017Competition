package org.usfirst.frc.team5895.robot.auto;

import org.usfirst.frc.team5895.robot.DriveTrain;
import org.usfirst.frc.team5895.robot.GearReceiver;
import org.usfirst.frc.team5895.robot.Shooter;
import org.usfirst.frc.team5895.robot.Turret;
import org.usfirst.frc.team5895.robot.Vision;
import org.usfirst.frc.team5895.robot.framework.Waiter;

public class RedGear {
		
	public static void run(DriveTrain drivetrain, GearReceiver gear, Turret turret, Shooter shooter, Vision vision) {

		drivetrain.auto_red_gearDrive();
		Waiter.waitFor(4000);
		drivetrain.arcadeDrive(0, 0);
		gear.openGear();
		Waiter.waitFor(500);
		gear.pushGear();
		shooter.setSpeed(3000);
		Waiter.waitFor(1000);
		drivetrain.driveStraight(5);
		Waiter.waitFor(2000);
		drivetrain.arcadeDrive(0, 0);
		turret.turnTo(turret.getAngle()+vision.getX());
		Waiter.waitFor(200);
		Waiter.waitFor(shooter::atSpeed, 2000);
		if(shooter.getSpeed() > 10) {
			shooter.shoot();
		}
	}
}