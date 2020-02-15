/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Mechanisms.CatzIndexer;
import frc.Mechanisms.CatzDriveTrain;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
<<<<<<< Updated upstream
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
=======
public class Robot extends TimedRobot 
{
  CatzIndexer indexer;
  CatzDriveTrain driveTrain;

  @Override
  public void robotInit() 
  {
    indexer =  new CatzIndexer();
    driveTrain = new CatzDriveTrain();
>>>>>>> Stashed changes
  }

  public void robotPeriodic()
  {
    SmartDashboard.putNumber("Ball Count", indexer.getBallCount());
    SmartDashboard.putString("Drive Train Gear Mode", driveTrain.getDriveTrainGearMode());
    SmartDashboard.putBoolean("Intake State", true);
  }
  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
