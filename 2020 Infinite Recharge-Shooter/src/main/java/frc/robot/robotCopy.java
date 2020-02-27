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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
public class robotCopy extends TimedRobot
{
  public static CatzDriveTrain driveTrain;
  public static CatzIntake     intake;
  public static CatzIndexer    indexer;
  public static CatzShooter    shooter;
  public static CatzClimber    climber;

  public static DataCollection dataCollection;

  private static XboxController xboxDrv;
  public static XboxController xboxAux;

  private static final int XBOX_DRV_PORT = 0;
  public static final int XBOX_AUX_PORT = 1;

  public static PowerDistributionPanel pdp;

  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public static ArrayList<CatzLog> dataArrayList; 

  private final int DPAD_UP = 0;
  private final int DPAD_DOWN = 180;
  private final int DPAD_LT = 270;
  private final int DPAD_RT = 90;

  double[] velocityData2 = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};
  

  @Override
  public void robotInit() 
  {
    
    //dataArrayList       = new ArrayList<CatzLog>();
    //dataCollection      = new DataCollection();
    //dataCollectionTimer = new Timer();

   // autonomousTimer     = new Timer();
    
    dataCollection.dataCollectionInit(dataArrayList);

    pdp = new PowerDistributionPanel();

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    xboxAux = new XboxController(XBOX_AUX_PORT);

    //driveTrain = new CatzDriveTrain();
    //indexer    = new CatzIndexer();
    shooter    = new CatzShooter();
    
    

  }

  @Override
  public void robotPeriodic() 
  {
  
    shooter.debugSmartDashboard();
    shooter.smartdashboard();

     //System.out.println("LT : " + driveTrain.getSrxMagLT() + "RT : " + driveTrain.getSrxMagRT());
  }


  @Override
  public void autonomousInit() 
  {
    /*
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_SHOOTER);
    dataCollection.startDataCollection();
*/
   // autonomousTimer.start();
  /*  while(autonomousTimer.get() <2)
    {
      driveTrain.arcadeDrive(1, 0);
    }

    driveTrain.arcadeDrive(0, 0); */
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
    dataCollection.setLogDataID(dataCollection.LOG_ID_SHOOTER);
    dataCollection.startDataCollection();

  }

  @Override
  public void teleopPeriodic()
  {
    
    if(xboxAux.getPOV() == DPAD_UP)
    {
      shooter.setTargetRPM(4500.0);
    }

    if(xboxAux.getPOV() == DPAD_LT)
    {
     shooter.setTargetRPM(5000.0);
    }

    if(xboxAux.getPOV() == DPAD_DOWN)
    {
      shooter.setTargetRPM(6000.0);
    }


   if(xboxAux.getAButton())
   {
     shooter.logTestData = true;
   }
   if(xboxAux.getBButton())
   {
     shooter.logTestData = false;
   }
    
   
   if(xboxAux.getPOV() == DPAD_RT)
   {
    shooter.shoot();
   }

   if(xboxAux.getStartButton())
   {
    shooter.shooterOff();
   }
  
   if(xboxAux.getYButton())
   {

   }

   if(xboxAux.getXButton())
   {

  }


  }  //End of Teleop Periodic
 
  
  @Override
  public void disabledInit() 
  {
    dataCollection.stopDataCollection();
    
    
    /*  for (int i = 0; i <dataArrayList.size();i++)
      {
         System.out.println(dataArrayList.get(i));
      }  */
    

    try 
    {
      dataCollection.exportData(dataArrayList);
    } catch (Exception e) 
    {
      e.printStackTrace();
    } 
    
  }

}