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
  //public static CatzShooter    shooter;
  //public static CatzClimber    climber;

  public static DataCollection dataCollection;

  private static XboxController xboxDrv;
  private static XboxController XboxAux;

  private static final int XBOX_DRV_PORT = 0;
  private static final int XBOX_AUX_PORT = 1;

  //public static PowerDistributionPanel pdp;

  public static Timer dataCollectionTimer;
  public static Timer autonomousTimer;

  public static ArrayList<CatzLog> dataArrayList; 

  @Override
  public void robotInit() 
  {
<<<<<<< Updated upstream
    pdp = new PowerDistributionPanel();
=======
    driveTrain = new CatzDriveTrain();
    indexer    = new CatzIndexer();
    //shooter    = new CatzShooter();
    intake     = new CatzIntake();
    //climber    = new CatzClimber();    
    
    //pdp = new PowerDistributionPanel();
>>>>>>> Stashed changes

    /*dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();

    dataCollectionTimer = new Timer();
<<<<<<< Updated upstream

    autonomousTimer = new Timer();
=======
    autonomousTimer     = new Timer();*/
>>>>>>> Stashed changes
    
    //dataCollection.dataCollectionInit(dataArrayList);

    xboxDrv = new XboxController(XBOX_DRV_PORT);
<<<<<<< Updated upstream
    XboxAux = new XboxController(XBOX_AUX_PORT);
=======
    xboxAux = new XboxController(XBOX_AUX_PORT);
   
    //create a path chooser
    /*SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORL, true);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORM, true);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORR, true);
      
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORL, false);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORM, false);
    SmartDashboard.putBoolean(CatzConstants.POSITION_SELECTORR, false);
    
    SmartDashboard.putBoolean("Use default autonomous?", false);
    */
    indexer.startIndexerThread();
>>>>>>> Stashed changes
  }

  @Override
  public void robotPeriodic() 
  {
<<<<<<< Updated upstream
=======
    SmartDashboard.putBoolean("exit",     indexer.indexerExitSwitch.get());
    SmartDashboard.putBoolean("entrance", indexer.indexerEntranceSwitch.get());

    /*
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
    */
>>>>>>> Stashed changes
  }

  @Override
  public void autonomousInit() 
  {
<<<<<<< Updated upstream
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
=======
    /*dataCollection.dataCollectionInit(dataArrayList);
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();*/
>>>>>>> Stashed changes
  }

  @Override
  public void autonomousPeriodic() 
  {
  }

  @Override
  public void teleopInit() 
  {
<<<<<<< Updated upstream
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_TRAIN);
    dataCollection.startDataCollection();
=======
    //driveTrain.instantiateDifferentialDrive();
    /*
    dataCollection.dataCollectionInit(dataArrayList);
    dataCollectionTimer.reset();
    dataCollectionTimer.start();
    dataCollection.setLogDataID(dataCollection.LOG_ID_DRV_STRAIGHT_PID);
    dataCollection.startDataCollection();*/
>>>>>>> Stashed changes
  }

  @Override
  public void teleopPeriodic()
  {
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
<<<<<<< Updated upstream
=======

    if(xboxDrv.getBumper(Hand.kLeft))
    {
      driveTrain.shiftToHighGear();
    }
    else if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.shiftToLowGear();
    }

    if(xboxDrv.getStickButton(Hand.kRight))
    {
      intake.deployIntake();
    }
    else if(xboxDrv.getStickButton(Hand.kLeft))
    {
      intake.stowIntake();
    }
    else
    {
      intake.stopDeploying();
    }

    if(xboxAux.getAButton())
    {
      intake.rollIntake();
    }
    else if(xboxAux.getBButton())
    {
      intake.stopRolling();
    }

    if(xboxAux.getXButtonPressed())
    {
      indexer.stopIndexerThread();
      indexer.indexerMotorOnly();
    }
    else if(xboxAux.getYButtonPressed())
    {
      indexer.indexerMotorOff();
      indexer.startIndexerThread();
    }



>>>>>>> Stashed changes
  }

  @Override
  public void disabledInit() 
  {
    /*dataCollection.stopDataCollection();
    
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
    } */
  }

}