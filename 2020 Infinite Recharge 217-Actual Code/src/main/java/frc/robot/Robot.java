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

  private static XboxController xboxAux;

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



  /**

   * This function is run when the robot is first started up and should be used

   * for any initialization code.

   */

  @Override

  public void robotInit() 

  {

    xboxDrv = new XboxController(XBOX_DRV_PORT);

    xboxAux = new XboxController(XBOX_AUX_PORT);



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

    SmartDashboard.putBoolean("Deployed:", Intake.getDeployedLimitSwitchState());

    SmartDashboard.putBoolean("Stowed:",   Intake.getStowedLimitSwitchState());

    DriveTrain.monitorEncoderPosition();

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

    SmartDashboard.putNumber("Distance Goal", 0);
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

    // ---------------------------------------------DriveTrain--------------------------------------------

    //DriveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));

    // ---------------------------------------------ROLLER---------------------------------------------



    if(xboxAux.getTriggerAxis(Hand.kLeft) >= 0.25)

    {

      Intake.rollIntake();

    }

    else

    {

      Intake.stopRolling();

    }



    // ---------------------------------------------Intake Limit Switches---------------------------------------------   



    if (Intake.getDeployedLimitSwitchState() == true)
    {
      Intake.deployed = true;
    }
    else
    {
      Intake.deployed = false;
    }



    if (Intake.getStowedLimitSwitchState() == true)
    {
      Intake.stowed = true;
    }
    else
    {
      Intake.stowed = false;
    }



    // ---------------------------------------------DEPLOY/STOW---------------------------------------------

    

   /* if (xboxDrv.getAButton() && Intake.deployed == false)
    //if (xboxDrv.getStickButtonPressed(Hand.kLeft) && Intake.deployed == false)
    {
      Intake.deployIntake();
    }



    else if (xboxDrv.getYButton() && Intake.stowed == false)
    //else if (xboxDrv.getStickButtonPressed(Hand.kRight) && Intake.stowed == false)
    {
      Intake.stowIntake();
    } */



    // ---------------------------------------------Drive Straight---------------------------------------------

    
    SmartDashboard.putBoolean("Running Distance Drive", DriveTrain.runningDistanceDrive);
    SmartDashboard.putNumber("Distance Moved", DriveTrain.distanceMoved);
    


    if(xboxDrv.getAButton())
    {
      DriveTrain.setDistanceGoal(120);
    }

    if(xboxDrv.getBButton())

    {
      DriveTrain.setTargetVelocity(0);
    }


    if(xboxDrv.getXButton())

    {
      DriveTrain.setTargetVelocity(16000);

    }



    if(xboxDrv.getBumper(Hand.kLeft))

    {

      DriveTrain.shiftToHighGear();

    }



    if(xboxDrv.getBumper(Hand.kRight))

    {

      DriveTrain.shiftToLowGear();

    }

  }



  @Override

  public void disabledInit() 
  {
  }
}