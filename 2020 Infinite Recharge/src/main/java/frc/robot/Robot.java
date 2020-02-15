/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.Mechanisms.CatzClimber;
import frc.Mechanisms.CatzDriveTrain;
import frc.Mechanisms.CatzIndexer;
import frc.Mechanisms.CatzIntake;
import frc.Mechanisms.CatzShooter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot 
{
  CatzClimber climber;
  CatzDriveTrain driveTrain;
  CatzShooter shooter;
  CatzIndexer indexer;
  CatzIntake intake;

  XboxController xboxAux;
  XboxController xboxMain;

  private static final double DPAD_UP = 0;
  private static final double DPAD_RT = 90;
  private static final double DPAD_DN = 180;
  private static final double DPAD_LT = 270;

  private static final double CLIMBER_SPEED = 30;

  private static final double NO_SHOOTER_VELOCITY = 0;
  private static final double SHOOTER_VELOCITY_A = 0.25;
  private static final double SHOOTER_VELOCITY_B = 0.5;
  private static final double SHOOTER_VELOCITY_C = 0.75;
  @Override
  public void robotInit() 
  {
    climber = new CatzClimber();
    driveTrain = new CatzDriveTrain();
    shooter = new CatzShooter();
    indexer = new CatzIndexer();
    intake = new CatzIntake();
    xboxAux = new XboxController(0);
    xboxMain = new XboxController(1);
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
    
    driveTrain.arcadeDrive(xboxMain.getY(Hand.kLeft), xboxMain.getX(Hand.kRight));

    if(xboxMain.getBumper(Hand.kRight))
    {
      driveTrain.shiftToLowGear();
    }
    else if(xboxMain.getBumper(Hand.kLeft))
    {
      driveTrain.shiftToHighGear();
    }

    intake.rollIntake(-xboxMain.getTriggerAxis(Hand.kLeft));
    intake.rollIntake(xboxMain.getTriggerAxis(Hand.kRight));

    climber.extendLightsaber(xboxAux.getY(Hand.kLeft));

    indexer.indexerOverride(xboxAux.getX(Hand.kRight));
      
    if(xboxAux.getStartButton())
    {
      climber.runClimber(CLIMBER_SPEED);
    }

    if(xboxAux.getPOV() == DPAD_UP)
    {
      shooter.setTargetVelocity(SHOOTER_VELOCITY_B);
    }
    else if(xboxAux.getPOV() == DPAD_RT)
    {
      shooter.setTargetVelocity(SHOOTER_VELOCITY_A);
    }
    else if(xboxAux.getPOV() == DPAD_DN)
    {
      shooter.setTargetVelocity(NO_SHOOTER_VELOCITY);
    }
    else if(xboxAux.getPOV() == DPAD_LT)
    {
      shooter.setTargetVelocity(SHOOTER_VELOCITY_C);
    }

    if(xboxAux.getAButton())
    {
      intake.deployIntake();
    }
    else if(xboxAux.getYButton())
    {
      intake.retractIntake();
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