/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  public static DigitalInput bumpSwitch1;
  public static DigitalInput bumpSwitch2;

  public static final boolean ON = false;
  public static final boolean OFF = true;

  public static boolean switch1State;
  public static boolean switch2State;

  public static int numberOfBallCounts;

  public static char previousState;
  public static char currentState;
  public static char ballCheck;

  public static CANSparkMax motor1;

  public static Timer t;

  public static XboxController xboxAux;


  

  @Override
  public void robotInit() 
  {
    bumpSwitch1 = new DigitalInput(0);
    bumpSwitch2 = new DigitalInput(1);

    motor1 = new CANSparkMax(4, MotorType.kBrushless);
    motor1.setIdleMode(IdleMode.kBrake);

    t = new Timer();

    xboxAux = new XboxController(1);

    numberOfBallCounts = 0;

    previousState = 'A';
    currentState  = 'A';

    switch1State = OFF;
    switch1State = OFF;
  }

  @Override
  public void robotPeriodic() 
  {
    switch1State = bumpSwitch1.get();
    switch2State = bumpSwitch2.get();

    if(switch1State == OFF &&  switch2State == OFF)
    {
      //Intake action
      System.out.println("Motor Stop(doing nothing)");
      previousState = currentState;
      currentState = 'A';
      motor1.set(0);

    } 
    else if(switch1State == ON &&  switch2State == OFF)
    {
      //Move Belt
      System.out.println("Start Motor");
      previousState = currentState;
      currentState = 'B';
      motor1.set(-0.5);   
      boolean done = false; 
      int count = 0;

     while(done == false)
      {
        if(bumpSwitch2.get() == ON)
        {
          done = true;
        } 
        else
        {
          Timer.delay(0.002);
          count++;
          if(count >1000)
          {
            done = true; 
          }
        }
      } 
    }
    else if (switch1State == OFF &&  switch2State == ON)
    {
      //Stop Belt
      System.out.println("Motor Stop");
      motor1.set(0.0);
      if(previousState == 'B')
      {
        numberOfBallCounts ++;
      } 
      else if(previousState == 'D')
      {
        numberOfBallCounts ++;
      }

      previousState = currentState;
      currentState = 'C';
    }
    else if (switch1State == ON && switch2State == ON)
    {
      //Move Belt
      System.out.println("Start Motor-Ball in indexer");
      previousState = currentState;
      currentState = 'D';
      motor1.set(-0.5);
    }

    SmartDashboard.putNumber("Number of Balls", numberOfBallCounts);
    
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() 
  {
  }

  @Override
  public void teleopPeriodic() 
  {
    if(xboxAux.getAButton()==true)
    { 
      t.start();
      while(t.get()<2)
      {
        motor1.set(0.3);
      }
      motor1.set(0.0);

      numberOfBallCounts = 0;
    }
  }

 


}
