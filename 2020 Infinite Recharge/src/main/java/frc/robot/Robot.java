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
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    if(xboxDrv.getAButton())
    {
      driveTrain.setTargetVelocity(0);
    }
    else if(xboxDrv.getBButton())
    {
      driveTrain.setTargetVelocity(10000);
    }
    else if(xboxDrv.getXButton())
    {
      driveTrain.setTargetVelocity(500);
    }
    else if(xboxDrv.getYButton())
    {
      driveTrain.setEncPosition(0);
    }
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
