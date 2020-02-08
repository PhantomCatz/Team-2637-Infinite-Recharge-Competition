/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import frc.Mechanisms.CatzDriveTrain;

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

  private static XboxController xboxDrv;
  private static XboxController XboxAux;
  public static PowerDistributionPanel pdp;

  public static Timer t;

  private final int XBOX_DRV_PORT = 0;
  private final int XBOX_AUX_PORT = 1;


  public static ArrayList<CatzLog> dataArrayList; 

  public static DataCollection dataCollection;

  public static Timer autonomousTimer;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    driveTrain = new CatzDriveTrain();

    xboxDrv = new XboxController(XBOX_DRV_PORT);
    XboxAux = new XboxController(XBOX_AUX_PORT);
    
    t = new Timer();

    pdp = new PowerDistributionPanel();

    dataArrayList = new ArrayList<CatzLog>();

    dataCollection = new DataCollection();
    
    dataCollection.dataCollectionInit(dataArrayList);

    autonomousTimer = new Timer();

  }

  @Override
  public void robotPeriodic() 
  {
    //System.out.println("LT : " + driveTrain.getSrxMagLT() + "RT : " + driveTrain.getSrxMagRT());
  }

  @Override
  public void autonomousInit() 
  {
    t.start();
    dataCollection.setDataType(1);
    dataCollection.startDataCollection();

    autonomousTimer.start();

    while(autonomousTimer.get() < 1)
    {
       driveTrain.arcadeDrive(1.0, 0);  
    }
    
    driveTrain.arcadeDrive(0, 0);

  }

  @Override
  public void autonomousPeriodic() 
  {
   /*. while(t.get()<5)
    {
      System.out.println("HIIIIIIIIIIIIIII");
    driveTrain.drvTrainMtrCtrlLTFrnt.set(ControlMode.PercentOutput, -0.3);
    } 
    driveTrain.drvTrainMtrCtrlLTFrnt.set(ControlMode.PercentOutput, 0.0);

    */

  }

  @Override
  public void teleopInit() 
  {
    t.start();
    dataCollection.setDataType(1);
    dataCollection.startDataCollection();
  }

  @Override
  public void teleopPeriodic() 
  {
  
    driveTrain.arcadeDrive(xboxDrv.getY(Hand.kLeft), xboxDrv.getX(Hand.kRight));
    if(xboxDrv.getBumper(Hand.kLeft)){
      driveTrain.retractGearShift();
    }
    if(xboxDrv.getBumper(Hand.kRight))
    {
      driveTrain.deployGearShift();
    }
  }

  @Override
  public void disabledInit() 
  {
    dataCollection.stopDataCollection();
    
    System.out.println("Data print out");
      for (int i = 0; i <dataArrayList.size();i++)
      {
         System.out.println(dataArrayList.get(i));
      }  

   /*   dataCollection.writeData(dataArrayList);
    try {
      dataCollection.exportData();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } */
  }  

  
}

