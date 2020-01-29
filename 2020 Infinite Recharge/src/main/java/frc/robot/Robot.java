/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Mechanisms.CatzDriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot 
{
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  CatzDriveTrain driveTrain;

  XboxController xboxDrv;

  @Override
  public void robotInit() 
  {
    driveTrain = new CatzDriveTrain();

    xboxDrv = new XboxController(0);
  }

  @Override
  public void robotPeriodic() 
  {
    SmartDashboard.putNumber("Enc Pos", driveTrain.getLTEncPosition());
    SmartDashboard.putNumber("Enc Vel", driveTrain.getLTEncVelocity());


  }

  @Override
  public void autonomousInit() 
  {
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {

  }

  @Override
  public void teleopPeriodic() 
  {
   /**
    * Choose a button to press on the controller, then press RT to submit the command to the motor.
    */

    if(xboxDrv.getBumper(Hand.kRight))
    //{
      //driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    //}
    if(xboxDrv.getAButtonPressed())
    {
      driveTrain.setTargetVelocity(0);
    }
    else if(xboxDrv.getBButtonPressed())
    {
      driveTrain.setTargetVelocity(50);
    }
    else if(xboxDrv.getXButtonPressed())
    {
      driveTrain.setEncPosition(500);
    }
    else if(xboxDrv.getYButtonPressed())
    {
      driveTrain.setEncPosition(0);
    }
   // read PID coefficients from SmartDashboard
        
   final double p = SmartDashboard.getNumber("P Gain", 0);
        
   final double i = SmartDashboard.getNumber("I Gain", 0);

   final double d = SmartDashboard.getNumber("D Gain", 0);

   

   final double f = SmartDashboard.getNumber("Feed Forward", 0);

  



   // if PID coefficients on SmartDashboard have changed, write new values to controller

   if((p != driveTrain.kP)) {driveTrain.setDriveKp(0,p); driveTrain.kP = p; }

   if((i != driveTrain.kI)) { driveTrain.setDriveKI(0,i); driveTrain.kI = i; }

   if((d != driveTrain.kD)) { driveTrain.setDriveKD(0,d); driveTrain.kD = d; }


   if((f != driveTrain.kF)) { driveTrain.setDriveKF(0,f); driveTrain.kF = f; }



   }
  

  @Override
  public void testInit() 
  {

  }

  @Override
  public void testPeriodic() 
  {

  }

}
