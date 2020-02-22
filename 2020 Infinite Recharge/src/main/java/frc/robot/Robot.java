/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

<<<<<<< Updated upstream
=======
import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
>>>>>>> Stashed changes
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.Mechanisms.CatzClimber;
import frc.Mechanisms.CatzDriveTrain;
import frc.Mechanisms.CatzIndexer;
import frc.Mechanisms.CatzIntake;
import frc.Mechanisms.CatzShooter;

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

  CatzDriveTrain drvTrain;
  CatzIndexer    indexer;
  CatzIntake     intake;
  CatzShooter    shooter;
  CatzClimber    climber;
  
  XboxController drv;
  XboxController aux;

  @Override
  public void robotInit() 
  {
    drvTrain = new CatzDriveTrain();
    indexer  = new CatzIndexer();
    intake   = new CatzIntake();
    shooter  = new CatzShooter();
    climber  = new CatzClimber();

    drv = new XboxController(0);
    aux = new XboxController(1);
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
<<<<<<< Updated upstream
=======
   driveTrain.setDriveTrainObjToNull();
   driveTrain.setTargetVelocity(3000);
>>>>>>> Stashed changes
  }

  @Override
  public void testInit() 
  {

  }

  @Override
  public void testPeriodic() 
  {

<<<<<<< Updated upstream
=======
    driveTrain.instatiateDriveTrainObj();
>>>>>>> Stashed changes
  }

  @Override
  public void disabledInit()
  {
<<<<<<< Updated upstream

=======
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    
>>>>>>> Stashed changes
  }

  @Override
  public void disabledPeriodic()
  {

  }
}