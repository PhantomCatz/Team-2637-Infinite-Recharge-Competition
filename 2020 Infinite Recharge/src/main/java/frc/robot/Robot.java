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
    SmartDashboard.putNumber("Integraded Enc Pos", driveTrain.getLTEncPosition());
    SmartDashboard.putNumber("Integraded Enc Vel", driveTrain.getLTEncVelocity());

    driveTrain.talonEncsPos();
    driveTrain.talonEncsVel();

    SmartDashboard.putNumber("Integraded Enc Linear Vel", driveTrain.getLTEncLinearVelocity());
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
    /*if(xboxDrv.getBumper(Hand.kRight))
    {
      //driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    }*/
    if(xboxDrv.getBumper(Hand.kLeft))
    {
      driveTrain.deployGearShift();
    }
    else if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.retractGearShift();
    }
    
    if(xboxDrv.getAButtonPressed())
    {
      driveTrain.setTargetVelocity(0);
    }
    else if(xboxDrv.getBButtonPressed())
    {
      driveTrain.setTargetVelocity(5000);
    }
    else if(xboxDrv.getXButtonPressed())
    {
      driveTrain.setTargetVelocity(1000);
    }
    else if(xboxDrv.getYButtonPressed())
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
