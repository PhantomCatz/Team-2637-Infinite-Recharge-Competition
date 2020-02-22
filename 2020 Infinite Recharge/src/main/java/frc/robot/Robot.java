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

  public static DataCollection dataCollection;

  private static XboxController xboxDrv;
  private static XboxController XboxAux;

  private static final int XBOX_DRV_PORT = 0;
  private static final int XBOX_AUX_PORT = 1;

  public static PowerDistributionPanel pdp;

  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public static ArrayList<CatzLog> dataArrayList; 

  @Override
  public void robotInit() 
  {
    pdp = new PowerDistributionPanel();

    dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();

    dataCollectionTimer = new Timer();

    autonomousTimer = new Timer();
    
    dataCollection.dataCollectionInit(dataArrayList);

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    XboxAux = new XboxController(XBOX_AUX_PORT);
  }

  @Override
  public void robotPeriodic() 
  {
  }

  @Override
  public void autonomousInit() 
  {
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_TRAIN);
    dataCollection.startDataCollection();

    autonomousTimer.start();
    while(autonomousTimer.get() <2)
    {
      driveTrain.arcadeDrive(1, 0);
    }

    driveTrain.arcadeDrive(0, 0);
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_TRAIN);
    dataCollection.startDataCollection();
  }

  @Override
  public void teleopPeriodic()
  {
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
  }

  @Override
  public void disabledInit() 
  {
    dataCollection.stopDataCollection();
    
      for (int i = 0; i <dataArrayList.size();i++)
      {
         System.out.println(dataArrayList.get(i));
      }  

    try 
    {
      dataCollection.exportData(dataArrayList);
    } catch (Exception e) 
    {
      e.printStackTrace();
    } 
  }

}