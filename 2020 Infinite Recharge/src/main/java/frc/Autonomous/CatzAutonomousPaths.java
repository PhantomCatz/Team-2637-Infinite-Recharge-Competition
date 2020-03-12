package frc.Autonomous;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.Autonomous.CatzAutonomous;
import frc.robot.*;
import frc.Mechanisms.*;

public class CatzAutonomousPaths
{
    static CatzAutonomous auton = new CatzAutonomous();

    public boolean spunUp = false;
    public boolean doneShooting = false;
    public static boolean done;

    public final int DRIVE_DIST_HIGH_SPEED = 16000;
    public final int DRIVE_DIST_MED_SPEED = 10000;
    public final int DRIVE_DIST_LOW_SPEED = 7500;
    public final int TURN_SPEED = 12000;

    public static boolean goalMet;

    public final static double STRAIGHT_BLACKTOP_POWER_PORT_DIST      = -78;
    public final static double STRAIGHT_BLACKTOP_PAST_START_LINE_DIST = 130;

    public final static int STRAIGHT_BLACKTOP_MED_SPEED  = 12000;
    public final static int STRAIGHT_BLACKTOP_HIGH_SPEED = 16000;

    public final static double SIDE_BLACKTOP_DRIVE_DIST = 24;
    
    public final static int SIDE_BLACKTOP_DRIVE_SPEED = 12000;

    public final static double DEFAULT_DIST = 60;

    public final static int DEFAULT_SPEED = 12000;

    public enum AUTO_STATE 
    {

        AS_INIT, AS_DRIVETO, AS_SHOOT, AS_DRIVEBACK, AS_TURN, AS_DONE;

    }

    public static AUTO_STATE autoState = AUTO_STATE.AS_INIT;

    public CatzAutonomousPaths() 
    {

        done = false;
    
    }

    /**
     * 
     * @param position - The path the robot will be taking (e.g. "STRAIGHT")
     */
    public static void monitorAutoState(String position) 
    {

        SmartDashboard.putString("Autonomous State", autoState.toString());  //prints the enum text to dashboard; keep track of current sequence TV 3/4/2020
        SmartDashboard.putBoolean("Goal Met", goalMet);

        if (position.equalsIgnoreCase("STRAIGHT"))
        {
            if(!done)
            {
                switch (autoState) 
                {
                    case AS_INIT:
                        auton.setDistanceGoal(STRAIGHT_BLACKTOP_POWER_PORT_DIST, STRAIGHT_BLACKTOP_MED_SPEED);

                        Robot.shooter.setTargetRPM(Robot.shooter.SHOOTER_TARGET_RPM_LO); //TODO LO??
                        
                        autoState = AUTO_STATE.AS_DRIVETO;
                    break;

                    case AS_DRIVETO:
                        //goalMet = auton.monitorEncoderPosition();
                        System.out.println("drive to" + goalMet + ", " + Robot.shooter.getShooterReadyState());
                        if (goalMet)// && Robot.shooter.getShooterReadyState()) 
                        {
                            if(Robot.shooter.getShooterReadyState())
                            {
                                autoState = AUTO_STATE.AS_SHOOT;
                            }
                        }
                        else
                        {
                            goalMet = auton.monitorEncoderPosition();
                        }
                    break;

                    case AS_SHOOT:
                        System.out.println("Shoot" + Robot.shooter.getShooterReadyState());
                        if (Robot.shooter.getShooterReadyState() == false)//.shooter.shooterState == Robot.shooter.SHOOTER_STATE_OFF) 
                        {
                            auton.setDistanceGoal(STRAIGHT_BLACKTOP_PAST_START_LINE_DIST, STRAIGHT_BLACKTOP_HIGH_SPEED);
                            autoState = AUTO_STATE.AS_DRIVEBACK;
                        }
                    break;

                    case AS_DRIVEBACK:
                        goalMet = auton.monitorEncoderPosition();
                        System.out.println("drive back");
                        if (goalMet) 
                        {
                            autoState = AUTO_STATE.AS_DONE;
                        }
                    break;

                    case AS_DONE:
                        System.out.println(position + " - DONE");
                        done = true;
                    break;
            
                }        
            }  
        }
        else if(position.equalsIgnoreCase("LEFT") || position.equalsIgnoreCase("RIGHT"))
        {

            if(!done)
            {

                switch (autoState)
                {

                    case AS_INIT:
                        auton.setDistanceGoal(SIDE_BLACKTOP_DRIVE_DIST, SIDE_BLACKTOP_DRIVE_SPEED);
                        autoState = AUTO_STATE.AS_DRIVETO;
                    break;
                    
                    case AS_DRIVETO:
                        goalMet = auton.monitorEncoderPosition();

                        if(goalMet)
                        {

                            autoState = AUTO_STATE.AS_DONE;

                        }

                    break;
                    
                    case AS_DONE:
                        System.out.println(position + " - DONE");

                        done = true;
                    break;

                }

            }

        }

        else
        {
            if(!done)
            {
                switch (autoState)
                {

                    case AS_INIT:
                        auton.setDistanceGoal(DEFAULT_DIST, DEFAULT_SPEED);
                        autoState = AUTO_STATE.AS_DRIVETO;
                        break;
                    
                    case AS_DRIVETO:
                        goalMet = auton.monitorEncoderPosition();

                        if(goalMet)
                        {

                            autoState = AUTO_STATE.AS_DONE;

                        }
                        break;
                    
                    case AS_DONE:
                        System.out.println(position + " DONE");
                        done = true;
                        break;
                }
        
            }
        
        }

    }

}