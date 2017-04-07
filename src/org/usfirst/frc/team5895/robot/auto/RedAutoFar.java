package org.usfirst.frc.team5895.robot.auto;

import org.usfirst.frc.team5895.robot.DriveTrain;
import org.usfirst.frc.team5895.robot.Intake;
import org.usfirst.frc.team5895.robot.Shooter;
import org.usfirst.frc.team5895.robot.Turret;
import org.usfirst.frc.team5895.robot.Vision;
import org.usfirst.frc.team5895.robot.framework.LookupTable;
import org.usfirst.frc.team5895.robot.framework.Waiter;

public class RedAutoFar {
	
	public static void run(DriveTrain drivetrain, Shooter shooter, Turret turret, LookupTable table, Vision vision, Intake intake) {
	
		turret.turnTo(80);
		intake.open();
		drivetrain.auto_red_farDrive();
		shooter.setSpeed(3250);
		Waiter.waitFor(drivetrain::isFinished, 4000);
		drivetrain.arcadeDrive(0, 0);
		vision.update();
		turret.turnTo(turret.getAngle()+vision.getX()-1);
		Waiter.waitFor(200);
		vision.update();
		turret.turnTo(turret.getAngle()+vision.getX()-1);
		Waiter.waitFor(shooter::atSpeed, 2000);
		if(shooter.getSpeed() > 10) {
			shooter.shoot();
			Waiter.waitFor(5000);
			intake.close();
			Waiter.waitFor(500);
			intake.open();
		}
	}

}
