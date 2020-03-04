package frc.Autonomous;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.Mechanisms.*;
import frc.robot.Robot;

public class CatzAutonomous
{

    public static boolean checkBoxL;
    public static boolean checkBoxM;
    public static boolean checkBoxR;

    public final double STOP_THRESHOLD_ANGLE = 2;
    public final double SLOW_THRESHOLD_ANGLE = 50;

    public boolean runningRadialTurn = false;
    public double angleGoal;
    public double angleToTurn;

    public double currentEncPosition;
    public double turnRateRadians;

    public double r1;
    public double r2;
    public double s1Dot;
    public double s2Dot;
    public double s1Conv;
    public double s2Conv;

    public Timer turnT;
    public Timer driveT1;
    public Timer driveTWait;

    public final double WAIT_TIME_THRESHOLD = 5.0;
    public double totalTime = 0;

    public int lVelocity;
    public int rVelocity;

    public double leftInitialEncoderPos;
    public double rightInitialEncoderPos;

    public boolean runningDistanceDrive = false;
    public boolean driveBackwards = false;
    public boolean monitoringTime = false;

    public double distanceGoal;
    public double distanceMoved;
    public final double STOP_THRESHOLD_DIST = 0.5;
    public final double SLOW_THRESHOLD_DIST = 30;

    public final double ENCODER_COUNTS_PER_INCH_LT = 1014.5; //without weight: 1046.6
    public final double ENCODER_COUNTS_PER_INCH_RT = 964; //without weight: 1025.7

    public final double TO_RADIANS = Math.PI/180;
    public final double TO_DEGREES = 180/Math.PI;

    private final double DRIVE_STRAIGHT_PID_TUNING_CONSTANT = 0.945; //0.98;

    public CatzAutonomous()
    {
        turnT = new Timer();
        driveT1 = new Timer();
        driveTWait = new Timer();
    }


    public void choosePath()
    {
        checkBoxL = SmartDashboard.getBoolean("Position Left", false);
        checkBoxM = SmartDashboard.getBoolean("Position Middle", false);
        checkBoxR = SmartDashboard.getBoolean("Position Right", false);

        //red and blue sides symmetrical, no check box for color required

        if(checkBoxL == true)
        {

        }
        else if(checkBoxM == true)
        {

        }
        else if(checkBoxR == true)
        {

        }
        else
        {
            System.out.println("no path was chosen - performing default");

            //drive past starting line and stop
        }
    }
    public void resetTotalTime()
    {
        totalTime = 0;
    }

    public void monitorTimer()
    {
        if(monitoringTime)
        {    
            if(driveTWait.get() > WAIT_TIME_THRESHOLD)
            {
                setDistanceGoal(103.5, 16000);
            }
        }
    }

    public void monitorEncoderPosition()
    {
        if (runningDistanceDrive == true)
        {
            currentEncPosition = CatzDriveTrain.getIntegratedEncPosition("LT");
            distanceMoved = Math.abs(leftEncoderDistanceMoved(currentEncPosition));
           
            SmartDashboard.putNumber("Encoder Position", currentEncPosition);
            SmartDashboard.putNumber("Initial Encoder Distance", leftInitialEncoderPos);
            SmartDashboard.putNumber("Distance Moved", distanceMoved);
            SmartDashboard.putNumber("Delta Encoder", (currentEncPosition - leftInitialEncoderPos));
            SmartDashboard.putNumber("drive timer", driveT1.get());

            System.out.println((currentEncPosition - leftInitialEncoderPos) + " = " + currentEncPosition + " - " + leftInitialEncoderPos);

            double distanceToGoal = distanceGoal - distanceMoved;

            SmartDashboard.putNumber("Distance To Goal", distanceToGoal);

            SmartDashboard.putNumber("total time", totalTime);
            if ((distanceToGoal) < STOP_THRESHOLD_DIST)
            {
                CatzDriveTrain.setTargetVelocity(0);
                driveT1.stop();
                driveTWait.reset();
                driveTWait.start();
                totalTime += driveT1.get();
                runningDistanceDrive = false;
                monitoringTime = true;
            }
            else if ((distanceToGoal) < SLOW_THRESHOLD_DIST && (distanceToGoal) < (distanceGoal*0.5))
            {
                //System.out.println("Starting slow: " + (getIntegratedEncVelocity("LT")*0.95));
                CatzDriveTrain.setTargetVelocity(CatzDriveTrain.getIntegratedEncVelocity("LT")*0.98);
                
            }
        }
    }

    public void setDistanceGoal(double inches, int speed)
    {
        if(!runningDistanceDrive)
        {   
            driveT1.reset();
            monitoringTime = false;
            runningDistanceDrive = true;
            leftInitialEncoderPos = CatzDriveTrain.drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(0);
            if (inches < 0)
            {
                driveT1.start();
                distanceGoal = -inches;
                CatzDriveTrain.setTargetVelocity(-speed);
            }
            else    
            {
                driveT1.start();
                distanceGoal = inches;
                CatzDriveTrain.setTargetVelocity(speed);
            }
        }
    }

    public void monitorAngle()
    {
        if (runningRadialTurn == true)
        {
            //double currentAngle = Robot.navx.getAngle();
/*            SmartDashboard.putNumber("Current Angle", currentAngle);
            SmartDashboard.putNumber("turn Time", turnT.get());

            angleToTurn = angleGoal - currentAngle;
*/
            if (angleToTurn < SLOW_THRESHOLD_ANGLE)
            {
                CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, lVelocity*0.99);
                CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -rVelocity*0.99);
            }
            else if(angleToTurn < STOP_THRESHOLD_ANGLE)
            {
                CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, 0);
                CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, 0);  
                turnT.stop();

                runningRadialTurn = false;
            }
        }
    }

    public void setAngleGoal(double angle, int leftVelocity, int rightVelocity)
    {
        if(!runningRadialTurn)
        {
            lVelocity= leftVelocity;
            rVelocity = rightVelocity;

            angleGoal = angle;
            runningRadialTurn = true;
            Robot.navx.reset();
            turnT.start();

            CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, leftVelocity);
            CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -rightVelocity);

            CatzDriveTrain.drvTrainMtrCtrlLTBack.follow(CatzDriveTrain.drvTrainMtrCtrlLTFrnt);
            CatzDriveTrain.drvTrainMtrCtrlRTBack.follow(CatzDriveTrain.drvTrainMtrCtrlRTFrnt);

            
        }
    }

    public void setVelocityTurn(double leftVelocity, double rightVelocity)
    {
        if(Robot.navx.getAngle() < 90)
        {
            CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, leftVelocity);
            CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -rightVelocity);

            CatzDriveTrain.drvTrainMtrCtrlLTBack.follow(CatzDriveTrain.drvTrainMtrCtrlLTFrnt);
            CatzDriveTrain.drvTrainMtrCtrlRTBack.follow(CatzDriveTrain.drvTrainMtrCtrlRTFrnt);
            turnT.reset();
            turnT.start();
        }
        double deltaTime = 0;
        double startTime = turnT.get();
        while(Robot.navx.getAngle() < 90) 
        {

            deltaTime = turnT.get() - startTime;
            SmartDashboard.putNumber("deltaTime", deltaTime);

            SmartDashboard.getNumber("navx angle", Robot.navx.getAngle());

            if (Robot.navx.getAngle() >= SLOW_THRESHOLD_ANGLE)
            {
                
                CatzDriveTrain.setTargetVelocity(CatzDriveTrain.getIntegratedEncVelocity("LT")*0.98);
                
            } 

        }

        CatzDriveTrain.setTargetVelocity(0);

        
    }

    public void radialTurn(double radiusOfCurvature, double turnRateDegrees, double targetAngleDegrees)
    {
        turnT.reset();

        r1 = radiusOfCurvature;
        r2 = radiusOfCurvature + (7.0/3.0);

        turnRateRadians = turnRateDegrees*TO_RADIANS;

        s1Dot = r1 * turnRateRadians;
        s2Dot = r2 * turnRateRadians;

        s1Conv = s1Dot * ENCODER_COUNTS_PER_INCH_RT * 12 *(1.0/10.0);
        s2Conv = s2Dot * ENCODER_COUNTS_PER_INCH_LT * 12 *(1.0/10.0);

        SmartDashboard.putNumber("s1", s1Dot);
        SmartDashboard.putNumber("s2", s2Dot);
        SmartDashboard.putNumber("s1Conv", s1Conv);
        SmartDashboard.putNumber("s2Conv", s2Conv);
        SmartDashboard.putNumber("Target Angle", targetAngleDegrees);
        SmartDashboard.putNumber("Turn Rate Degrees", turnRateDegrees);

        double targetAngleRadians = targetAngleDegrees * TO_RADIANS;
        double timeOut = (targetAngleRadians)/turnRateRadians;
       
        turnT.start();

        SmartDashboard.putNumber("Time Out", timeOut);

        double deltaTime;
        double timeStart = turnT.get();

        while ((turnT.get() - timeStart) < 2)
        {
            CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, s2Conv);
            CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -s1Conv * DRIVE_STRAIGHT_PID_TUNING_CONSTANT);

            CatzDriveTrain.drvTrainMtrCtrlLTBack.follow(CatzDriveTrain.drvTrainMtrCtrlLTFrnt);
            CatzDriveTrain.drvTrainMtrCtrlRTBack.follow(CatzDriveTrain.drvTrainMtrCtrlRTFrnt);

            deltaTime = turnT.get() - timeStart;
            SmartDashboard.putNumber("Delta time", deltaTime);
        }
        if(turnT.get() > timeOut)
        {
            CatzDriveTrain.drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, 0);
            CatzDriveTrain.drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, 0);
        }

    }

    public double leftEncoderDistanceMoved(double encoderPosition)
    {  
        //System.out.println((getIntegratedEncPosition("LT") - leftInitialEncoderPos) + " = " + getIntegratedEncPosition("LT") + " - " + leftInitialEncoderPos);
        return (encoderPosition - leftInitialEncoderPos) / ENCODER_COUNTS_PER_INCH_LT;
    }

    public double rightEncoderDistanceMoved()
    {   
        rightInitialEncoderPos = CatzDriveTrain.drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
        return (CatzDriveTrain.getIntegratedEncPosition("RT") - rightInitialEncoderPos) * ENCODER_COUNTS_PER_INCH_RT;
    }

}