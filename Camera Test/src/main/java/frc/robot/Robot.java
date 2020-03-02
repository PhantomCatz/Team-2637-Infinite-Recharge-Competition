/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
public class Robot extends TimedRobot
{
  public static CatzDriveTrain driveTrain;
  public static CatzIntake     intake;
  public static CatzIndexer    indexer;
  public static CatzShooter    shooter;
  public static CatzClimber    climber;

  private UsbCamera camera1;
  private UsbCamera camera2;
  private VideoSink server;

  private NetworkTableEntry cameraSelection;

  private int cameraID = 0;

  public static DataCollection dataCollection;

  private static XboxController xboxDrv;
  private static XboxController xboxAux;

  private static final int XBOX_DRV_PORT = 0;
  private static final int XBOX_AUX_PORT = 1;

  public static PowerDistributionPanel pdp;

  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public static ArrayList<CatzLog> dataArrayList; 

  public boolean testFlag = false;

  private static double cameraResolutionWidth = 320;
  private static double cameraResolutionHeight = 240;
  private static double cameraFPS = 15;
  

  private final int RPM_RANGE_MIN = 4100;
  private final int RPM_RANGE_MAX = 4300;

  @Override
  public void robotInit() 
  {
    pdp = new PowerDistributionPanel();

    dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();

    dataCollectionTimer = new Timer();

    autonomousTimer = new Timer();

    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setPixelFormat(PixelFormat.kYUYV);
    //camera1 = new UsbCamera("camera1", 0);
    
    camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    camera2.setPixelFormat(PixelFormat.kYUYV);
    //camera2 = new UsbCamera("camera2", 1);

    server =  CameraServer.getInstance().getServer();

    //cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
    
    //dataCollection.dataCollectionInit(dataArrayList);

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    /*xboxAux = new XboxController(XBOX_AUX_PORT);

    driveTrain = new CatzDriveTrain();
    indexer    = new CatzIndexer();
    shooter    = new CatzShooter();
    intake     = new CatzIntake();
    climber    = new CatzClimber(); */
  }

  @Override
  public void robotPeriodic() 
  {
   // indexer.runIndexer2();
    //indexer.showSmartDashboard();

    /*SmartDashboard.putNumber("RPM", shooter.getFlywheelShaftVelocity());
    SmartDashboard.putNumber("Power", shooter.getShooterPower());
    SmartDashboard.putNumber("Target Velocity", shooter.getTargetVelocity());*/

    camera1.setResolution((int)cameraResolutionWidth, (int)cameraResolutionHeight);
    camera1.setFPS((int)cameraFPS);

    camera2.setResolution((int)cameraResolutionWidth, (int)cameraResolutionHeight);
    camera2.setFPS((int)cameraFPS);
    
  }

  @Override
  public void autonomousInit() 
  {
    dataCollection.dataCollectionInit(dataArrayList);
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {

    dataCollection.dataCollectionInit(dataArrayList);
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();

    
  }

  @Override
  public void teleopPeriodic()
  {

    if(xboxDrv.getAButton())
    {

      server.setSource(camera1);

    }

    if(xboxDrv.getBButton())
    {

      server.setSource(camera2);

    }

    /*driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));

    if(xboxDrv.getBButton())
    {
      driveTrain.setTargetVelocity(0);
    }
    if(xboxDrv.getAButton())
    {
      driveTrain.setTargetVelocity(8000);
    }

    if(xboxDrv.getBumper(Hand.kLeft))
    {
      driveTrain.shiftToHighGear();
    }
    if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.shiftToLowGear();
    }

  /*  if(xboxDrv.getXButton()){
      shooter.oneQuarterPower();
    }
    if(xboxDrv.getAButton()){
      testFlag = true;
      
    }
    else if(xboxDrv.getBButton()){
      shooter.stopMotor();
    }
    if (testFlag) {
      shooter.testShoot();
    } */
    /*intake.rollIntake(xboxAux.getTriggerAxis(Hand.kLeft)-xboxAux.getTriggerAxis(Hand.kRight));

    //indexer.runIndexer2();

    intake.deployIntake(xboxAux.getY(Hand.kLeft) * 0.5);*/

    /*if(xboxDrv.getAButton())
    {
      camera1 = CameraServer.getInstance().startAutomaticCapture(0);
      camera1.setPixelFormat(PixelFormat.kYUYV);
    }*/

    
    

    /*if (xboxDrv.getAButton())
    {
      System.out.println("A button");
      
      //camera1.equals(camera2);
      

    }
    else if (xboxDrv.getBButton())
    {
      System.out.println("B button");
      //camera1.equals(camera1);
      
    }*/

  }
  
  @Override
  public void disabledInit() 
  {
    dataCollection.stopDataCollection();

    //indexer.printTraceData();
    
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