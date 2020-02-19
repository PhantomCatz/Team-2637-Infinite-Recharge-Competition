/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Autonomous.CatzAutonomous;
import frc.DataLogger.CatzLog;
import frc.DataLogger.DataCollection;
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
  public static CatzDriveTrain driveTrain;
  public static CatzIntake     intake;
  public static CatzIndexer    indexer;
  public static CatzShooter    shooter;
  public static CatzClimber    climber;
  public static CatzAutonomous autonomous;

  public static DataCollection     dataCollection;
  public static Timer              dataCollectionTimer;
  public static ArrayList<CatzLog> dataArrayList;

  private static XboxController xboxDrv;
  private static XboxController XboxAux;

  private static final int XBOX_DRV_PORT = 0;
  private static final int XBOX_AUX_PORT = 1;

  public static PowerDistributionPanel pdp;

  public static Timer autonomousTimer; 
  
  int testing = shooter.SHOOT_IDLE_MODE;  //tbd delete from final


  @Override
  public void robotInit() 
  {

    dataArrayList       = new ArrayList<CatzLog>();
    dataCollection      = new DataCollection();
    dataCollectionTimer = new Timer();
    dataCollection.dataCollectionInit(dataArrayList);

    autonomousTimer     = new Timer();

    pdp = new PowerDistributionPanel();

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    XboxAux = new XboxController(XBOX_AUX_PORT);

    driveTrain = new CatzDriveTrain();
    intake     = new CatzIntake();
    indexer    = new CatzIndexer();
    shooter    = new CatzShooter();
    climber    = new CatzClimber();

  }

  @Override
  public void robotPeriodic() 
  {
  }


  @Override
  public void autonomousInit() 
  {
    dataCollection.dataInit();

    autonomousTimer.start();

    autonomous.choosePath();    
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {
    dataCollection.dataInit();
  }

  @Override
  public void teleopPeriodic()
  {
    
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));

    if(XboxAux.getAButton())
    {
      intake.deployIntake(-0.5);   //tbd placeholder value
    }

    if(XboxAux.getYButton())
    {
      intake.deployIntake(0.5);   //tbd placeholder value
    }

    if(XboxAux.getPOV(CatzConstants.XBOX_POV_DN) == CatzConstants.XBOX_POV_DN)
    {
      shooter.shooterFlyWheelDisable();
    }

    if(XboxAux.getRawButton(CatzConstants.XBOX_START_BTN))
    {
      climber.runClimber(0.75);   //tbd placeholder value
    }

    climber.extendLightsaber(XboxAux.getY(Hand.kLeft));

    if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.shiftToLowGear();
    }

    if(xboxDrv.getBumper(Hand.kLeft))
    {
      driveTrain.shiftToHighGear();
    }

    intake.rollIntake(-xboxDrv.getTriggerAxis(Hand.kRight));
    intake.rollIntake(xboxDrv.getTriggerAxis(Hand.kLeft));
  }

    
  
  
  @Override
  public void disabledInit() 
  {
    dataCollection.stopDataCollection();

    try 
    {
      dataCollection.exportData(dataArrayList);
    } catch (Exception e) 
    {
      e.printStackTrace();
    } 
  }

}