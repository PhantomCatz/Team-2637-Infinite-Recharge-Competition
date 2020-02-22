<<<<<<< Updated upstream
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

=======
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

  public boolean STARTING_POSITION_LEFT   = false;
  public boolean STARTING_POSITION_MIDDLE = false;
  public boolean STARTING_POSITION_RIGHT  = false;

  //public boolean leftPosition;
  //public boolean middlePosition;
  //public boolean rightPosition;

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

    SmartDashboard.putBoolean("Starting Position: Left", STARTING_POSITION_LEFT);
    SmartDashboard.putBoolean("Starting Position: Middle", STARTING_POSITION_MIDDLE);
    SmartDashboard.putBoolean("Starting Position: Right", STARTING_POSITION_RIGHT);
  }

  @Override
  public void robotPeriodic() 
  {          
    if (SmartDashboard.getBoolean("Starting Position: Middle", STARTING_POSITION_MIDDLE) == true)
    {
      SmartDashboard.putBoolean("Starting Position: Left", STARTING_POSITION_LEFT);
      SmartDashboard.putBoolean("Starting Position: Right", STARTING_POSITION_RIGHT);
    }

    if (SmartDashboard.getBoolean("Starting Position: Left", STARTING_POSITION_LEFT) == true)
    {
      SmartDashboard.putBoolean("Starting Position: Middle", STARTING_POSITION_MIDDLE);
      SmartDashboard.putBoolean("Starting Position: Right", STARTING_POSITION_RIGHT);
    }

    if (SmartDashboard.getBoolean("Starting Position: Right", STARTING_POSITION_RIGHT) == true)
    {
      SmartDashboard.putBoolean("Starting Position: Middle", STARTING_POSITION_MIDDLE);
      SmartDashboard.putBoolean("Starting Position: Left", STARTING_POSITION_LEFT);
    }
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

    if (SmartDashboard.getBoolean("Starting Position: Left", STARTING_POSITION_LEFT) == true)
    {
      //run left path code
    }
    else if (SmartDashboard.getBoolean("Starting Position: Middle", STARTING_POSITION_MIDDLE) == true)
    {
      //run middle path code
    }
    else if (SmartDashboard.getBoolean("Starting Position: Right", STARTING_POSITION_RIGHT) == true)
    {
      //run right path code
    }
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

>>>>>>> Stashed changes
}