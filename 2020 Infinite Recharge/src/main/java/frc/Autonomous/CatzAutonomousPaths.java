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

    public CatzAutonomousPaths()
    {

        driveBackTimer = new Timer();
        shooterTimer = new Timer();

    }

    public void pathOne()
    {

        driveBackTimer.start();
        shooterTimer.start();
        auton.setDistanceGoal(-103.5, DRIVE_DIST_MED_SPEED);

        if(shooterTimer.get() > 2.7)
        {

            spunUp = true;

        }

        if(shooterTimer.get() > 7.7)
        {

            doneShooting = true;
            auton.setDistanceGoal(120, DRIVE_DIST_HIGH_SPEED);
            shooterTimer.stop();
            done = true;
            driveBackTimer.stop();

        }

        if(driveBackTimer.get() > 13 && !done)
        {

            auton.setDistanceGoal(120, DRIVE_DIST_HIGH_SPEED);
            done = true;
            driveBackTimer.stop();

        }
        


    }



}