package frc.Autonomous;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.Autonomous.CatzAutonomous;
import frc.Mechanisms.*;

public class CatzAutonomousPaths
{
    CatzAutonomous auton = new CatzAutonomous();
    CatzShooter shooter = new CatzShooter();

    public Timer driveBackTimer;
    public Timer shooterTimer;

    public boolean spunUp       = false;
    public boolean doneShooting = false;
    public boolean done         = false;

    public final int DRIVE_DIST_HIGH_SPEED = 16000;
    public final int DRIVE_DIST_MED_SPEED  = 10000;
    public final int DRIVE_DIST_LOW_SPEED  = 7500;
    public final int TURN_SPEED            = 12000;

    public boolean goalMet;

    public enum AUTO_STATE
    {
        AS_INIT,
        AS_DRIVETO, 
        AS_SHOOT, 
        AS_DRIVEBACK,
        AS_TURN, 
        AS_DONE;
    }

    public AUTO_STATE autoState =  AUTO_STATE.AS_INIT;

    public CatzAutonomousPaths()
    {

        driveBackTimer = new Timer();
        shooterTimer = new Timer();
        System.out.println("AS_INIT: " + autoState.ordinal());

    }

    public void monitorAutoStateP1()
    {

        SmartDashboard.putNumber("Autonomous State", autoState.ordinal());
        switch (autoState)
        {
            case AS_INIT:
                auton.setDistanceGoal(-103.5, 10000);
                autoState = AUTO_STATE.AS_DRIVETO;
                break;

            case AS_DRIVETO:
                goalMet = auton.monitorEncoderPosition();

                if(goalMet)
                {

                    auton.shooterTimer.reset();
                    auton.shooterTimer.start();
                    autoState =  AUTO_STATE.AS_SHOOT;
                        
                }
                break;

            case AS_SHOOT:
                if(auton.simulateShoot())
                {

                    auton.setDistanceGoal(120, 16000);
                    autoState = AUTO_STATE.AS_DRIVEBACK;
                }
                break;
            
            case AS_DRIVEBACK:
                goalMet = auton.monitorEncoderPosition();
                
                if(goalMet)
                {

                    autoState = AUTO_STATE.AS_DONE;

                }
                break;

            case AS_DONE:
                System.out.println("Done");
                break;
        }  

    }

    public void monitorAutoStateP2()
    {

        SmartDashboard.putNumber("Autonomous State", autoState.ordinal());

        switch(autoState)
        {

            case AS_INIT:
                auton.setAngleGoal(45, 16000, 0.5);
                autoState = AUTO_STATE.AS_TURN;
                break;
            
            case AS_TURN:
                goalMet = auton.monitorAngle();

                if(goalMet)
                {

                    auton.shooterTimer.reset();
                    auton.shooterTimer.start();
                    autoState = AUTO_STATE.AS_SHOOT;

                }
                break;

            case AS_SHOOT:
                goalMet = auton.simulateShoot();
                
                if(goalMet)
                {

                    auton.setDistanceGoal(-120, 16000);
                    autoState = AUTO_STATE.AS_DRIVEBACK;

                }
                break;
            
            case AS_DRIVEBACK:
                goalMet = auton.monitorEncoderPosition();
                    
                if(goalMet)
                {

                    autoState = AUTO_STATE.AS_DONE;

                }
                break;
            
            case AS_DONE:
                System.out.println("Done");
                break;
        }

    }

}