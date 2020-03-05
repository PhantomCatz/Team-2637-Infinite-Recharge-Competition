/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Autonomous.CatzAutonomous;
import frc.Autonomous.CatzAutonomousPaths;
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
  public static CatzAutonomousPaths autonPaths;
  public static CatzAutonomous auton;
  public static CatzDriveTrain driveTrain;
  public static CatzIntake     intake;
  public static CatzIndexer    indexer;
  public static CatzShooter    shooter;
  public static CatzClimber    climber;

  public DataCollection dataCollection;

  private  XboxController xboxDrv;
  private  XboxController xboxAux;

  private final int XBOX_DRV_PORT = 0;
  private final int XBOX_AUX_PORT = 1;

  public static PowerDistributionPanel pdp;

  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public ArrayList<CatzLog> dataArrayList; 

  public boolean testFlag = false;

  public static AHRS navx;

  public boolean check_boxL = false;
  public boolean check_boxM = false;
  public boolean check_boxR = false;

	public boolean prev_boxL = false;
	public boolean prev_boxM = false;
	public boolean prev_boxR = false;

  @Override
  public void robotInit() 
  {

    auton      = new CatzAutonomous();
    autonPaths = new CatzAutonomousPaths();
    driveTrain = new CatzDriveTrain();
    /*indexer    = new CatzIndexer();
    shooter    = new CatzShooter();
    intake     = new CatzIntake();
    climber    = new CatzClimber();*/ 
    
    pdp        = new PowerDistributionPanel();

    dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();

    dataCollectionTimer = new Timer();
    autonomousTimer     = new Timer();
    
    dataCollection.dataCollectionInit(dataArrayList);

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    xboxAux = new XboxController(XBOX_AUX_PORT);

    driveTrain.setMotorsToCoast();
   
    navx = new AHRS(SPI.Port.kMXP, (byte)200);  // whc 03Mar20 - do we really want the update rate this high?

    //create a path chooser
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORL, true);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORM, true);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORR, true);
      
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORL, false);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORM, false);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORR, false);
    
    SmartDashboard.putBoolean("Use default autonomous?", false);

    navx.reset();

    auton.driveT1.reset();
  
  }

  @Override
  public void robotPeriodic() 
  {
    //path chooser safety code
    check_boxL = SmartDashboard.getBoolean(CatzConstants.POSITION_SELECTORL, false);
		check_boxM = SmartDashboard.getBoolean(CatzConstants.POSITION_SELECTORM, false);
		check_boxR = SmartDashboard.getBoolean(CatzConstants.POSITION_SELECTORR, false);

    if ((check_boxL != prev_boxL) && (check_boxL == true)) 
    {
			prev_boxL = check_boxL;
			prev_boxM = false;
			prev_boxR = false;
    }
     else if ((check_boxM != prev_boxM) && (check_boxM == true)) 
    {
			prev_boxL = false;
			prev_boxM = check_boxM;
			prev_boxR = false;
    } 
    else if ((check_boxR != prev_boxR) && (check_boxR == true)) 
    {
			prev_boxL = false;
			prev_boxM = false;
			prev_boxR = check_boxR;
    }
    
		// Update display
		SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORL, prev_boxL);
		SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORM, prev_boxM);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORR, prev_boxR);
    
    //SmartDashboard.putNumber("NavxAngle", navx.getAngle());

  }

  @Override
  public void autonomousInit() 
  {
    dataCollection.dataCollectionInit(dataArrayList); // whc 03Mar20 - this dataCollection was already initialized on line 93
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();

  }

  @Override
  public void autonomousPeriodic() 
  {

    autonPaths.monitorAutoStateP2();

    SmartDashboard.putNumber("Drive Back Timer", autonPaths.driveBackTimer.get());
    SmartDashboard.putNumber("Shooter Timer", autonPaths.shooterTimer.get());
    SmartDashboard.putBoolean("Spun Up", autonPaths.spunUp);
    SmartDashboard.putBoolean("Done Shooting", autonPaths.doneShooting);
    SmartDashboard.putBoolean("Done", autonPaths.done);

  }

  @Override
  public void teleopInit() 
  {

    driveTrain.instantiateDifferentialDrive();
    //driveTrain.setDriveTrainPIDConfiguration();

    dataCollection.dataCollectionInit(dataArrayList);
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();

  }

  @Override
  public void teleopPeriodic()
  {

    //SmartDashboard.putNumber("Angle", navx.getAngle());

    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    //auton.monitorEncoderPosition();
    //auton.monitorTimer();

    if(xboxDrv.getBumper(Hand.kLeft))
    {
      driveTrain.shiftToHighGear();
    }

    if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.shiftToLowGear();
    }

    if(xboxDrv.getAButton())
    {
      //driveTrain.setDistanceGoal(103.5);
    }  
    
    if(xboxDrv.getBButton())
    {
      driveTrain.setTargetVelocity(0);
    }

    if(xboxDrv.getXButton())
    {

      auton.resetTotalTime();
      auton.setDistanceGoal(-103.5, 10000);
      //driveTrain.radialTurn(6.25, 90, 90);
      
      //driveTrain.setAngleGoal(90, 16000, 8000);

    }

    if(xboxDrv.getTriggerAxis(Hand.kLeft) == 1)
    {

      driveTrain.setMotorsToCoast();

    }

  }
  
  @Override
  public void disabledInit() 
  {

    //driveTrain.setMotorsToBrake();
    dataCollection.stopDataCollection();
    
   /* for (int i = 0; i <dataArrayList.size();i++)
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