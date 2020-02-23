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
import frc.Mechanisms.CatzShooter;
import frc.DataLogger.CatzLog;
import frc.DataLogger.DataCollection;
import frc.Mechanisms.CatzClimber;
import frc.Mechanisms.CatzDriveTrain;
import frc.Mechanisms.CatzIntake;
import frc.Mechanisms.CatzIndexer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot 
{
  //public static CatzDriveTrain driveTrain; 

  private static XboxController xboxDrv;
  private static XboxController XboxAux;
  public static PowerDistributionPanel pdp;

  public static Timer t;

  private final int XBOX_DRV_PORT = 0;
  private final int XBOX_AUX_PORT = 1;

  public static CatzShooter Shooter;
  public static CatzIndexer Indexer;
  public static CatzIntake Intake;
  public static CatzClimber Climber;
  public static CatzDriveTrain DriveTrain;

  public static DataCollection dataCollection;
  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public static ArrayList<CatzLog> dataArrayList; 

  public static double testPower = 0;

  double c;

  private boolean readyToFire =     false;

  private final int RPM_RANGE_MIN = 4100;
  private final int RPM_RANGE_MAX = 4300;

  private final double TARGET_VELOCITY = 20000;

  boolean deployed = false;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    xboxDrv = new XboxController(XBOX_DRV_PORT);
    XboxAux = new XboxController(XBOX_AUX_PORT);

    autonomousTimer = new Timer();

    Shooter    = new CatzShooter();
    Indexer    = new CatzIndexer();
    Intake     = new CatzIntake();
    Climber    = new CatzClimber();
    DriveTrain = new CatzDriveTrain();

    dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();

    dataCollectionTimer = new Timer();

    autonomousTimer = new Timer();
    
    dataCollection.dataCollectionInit(dataArrayList); 
  }

  @Override
  public void robotPeriodic() 
  {
   SmartDashboard.putNumber("RPM:",    Shooter.getFlywheelShaftVelocity() );
   SmartDashboard.putNumber("Counts",  Shooter.getFlywheelShaftPosition() );
   SmartDashboard.putNumber("Test Power",    testPower);
   SmartDashboard.putBoolean("Ready to Fire", readyToFire);

   // code to signal when the rpm is within range
   if (Math.abs(Shooter.getFlywheelShaftVelocity()) > RPM_RANGE_MIN && Math.abs(Shooter.getFlywheelShaftVelocity()) < RPM_RANGE_MAX)
   {
      readyToFire = true;
   }

   SmartDashboard.putNumber("RPM2:",    Shooter.getFlywheelShaftVelocity() );
   SmartDashboard.putNumber("Counts2",  Shooter.getFlywheelShaftPosition() );
   SmartDashboard.putNumber("Test Power2",    testPower);
  }

  @Override
  public void autonomousInit() 
  {/*
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_TRAIN);
    dataCollection.startDataCollection();

    autonomousTimer.start();
    while(autonomousTimer.get() <2)
    {
      DriveTrain.arcadeDrive(1, 0);
    }

    DriveTrain.arcadeDrive(0, 0);*/
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {
    /*
    t.start();
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_TRAIN);
    dataCollection.startDataCollection();*/
  }

  @Override
  public void teleopPeriodic() 
  {
    DriveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));

    /*if (xboxDrv.getAButton())
    {
     //testPower = 0.7 ;
     //Shooter.testShootPower(testPower);
     Shooter.setTargetVelocity(TARGET_VELOCITY);
    }

    else if (xboxDrv.getXButton())
    {
      testPower += 0.02;
      Shooter.testShootPower(testPower);
      Timer.delay(0.7);
    }
    
    else if (xboxDrv.getYButton())
    {
      testPower -= 0.02;
      Shooter.testShootPower(testPower);
      Timer.delay(0.7);
    }

    else if (xboxDrv.getBButton())
    {
      Shooter.stopMotor();
    }*/


    /*if (xboxDrv.getBButton())
    {
      Intake.stopRolling();
    }*/
    
    if (xboxDrv.getAButton())
    {
      deployed = true;
      Intake.deployIntake();
      SmartDashboard.putBoolean("Deployed", deployed);
    }

    else if (xboxDrv.getYButton())
    {
      deployed = false;
      Intake.stowIntake();
      SmartDashboard.putBoolean("Deployed:", deployed);
    }
    
    else
    {
      Intake.stopDeploying();
    }

    if(xboxDrv.getTriggerAxis(Hand.kLeft) >= 0.25)
    {
      Intake.rollIntake();
    }
    else
    {
      Intake.stopRolling();
    }
  }

  @Override
  public void disabledInit() 
  {
    DataCollection.stopDataCollection();
    
    for (int i = 0; i <dataArrayList.size();i++)
    {
       System.out.println(dataArrayList.get(i));
    }  

  try 
  {
    DataCollection.exportData(dataArrayList);
  } catch (Exception e) 
  {
    e.printStackTrace();
  } 
  }  

  
}