package Movement;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import Util.*;

public class TurnControl implements PIDOutput
{
	
	double rotateToAngleRate;
	AHRS navx;
	PIDController turnController;
	
	static double kP = 0.00;
	static double kI = 0.00;
	static double kD = 0.00;
	static double kF = 0.00;
	static double kToleranceDegrees = 0;
	
	public TurnControl()
	{
		Constants cons = new Constants();
		cons.LoadConstants();
		
		kP = cons.GetDouble("kP");
		kI = cons.GetDouble("kI");
		kD = cons.GetDouble("kD");
		kF = cons.GetDouble("kF");
		kToleranceDegrees = cons.GetDouble("kToleranceDegrees");
		
		
		try 
		{
			navx = new AHRS(SPI.Port.kMXP);
		}
		catch (RuntimeException ex )
		{
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}
		
		turnController = new PIDController(kP, kI, kD, kF, navx, this);
		turnController.setInputRange(-180.0f,  180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
	}
	
	public void SetAngle(double value)
	{
		navx.reset();
		turnController.setSetpoint(value * 1f);
	}
	
	public void SetSpeed(double speed)
	{
		turnController.setOutputRange(-speed, speed);
	}
	
	public double GetRotateRate()
	{
		return rotateToAngleRate;
	}
	
	public boolean onTarget()
	{
		return turnController.onTarget();
	}

	public void pidWrite(double output)
	{
		rotateToAngleRate = output;
	}
}