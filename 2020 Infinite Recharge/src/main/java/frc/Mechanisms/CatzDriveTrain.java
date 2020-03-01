package frc.Mechanisms;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class CatzDriveTrain
{
    public WPI_TalonFX drvTrainMtrCtrlLTFrnt;
    public WPI_TalonFX drvTrainMtrCtrlLTBack;
    public WPI_TalonFX drvTrainMtrCtrlRTFrnt;
    public WPI_TalonFX drvTrainMtrCtrlRTBack;

    public final int DRVTRAIN_LT_FRNT_MC_CAN_ID = 1;
    public final int DRVTRAIN_LT_BACK_MC_CAN_ID = 2;

    public final int DRVTRAIN_RT_FRNT_MC_CAN_ID = 3;
    public final int DRVTRAIN_RT_BACK_MC_CAN_ID = 4;

    public final int DRV_TRN_LT_FRNT_MC_PDP_PORT = 0;
    public final int DRV_TRN_LT_BACK_MC_PDP_PORT = 1;
    public final int DRV_TRN_RT_FRNT_MC_PDP_PORT = 15;
    public final int DRV_TRN_RT_BACK_MC_PDP_PORT = 14;

   private DifferentialDrive drvTrainDifferentialDrive;

    private SpeedControllerGroup drvTrainLT;
    private SpeedControllerGroup drvTrainRT;

    private DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PORT_A_PCM = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PORT_B_PCM = 1;

    private final double GEAR_RATION      = 11/44;
    private final double LOW_GEAR_RATION  = 14/60;
    private final double HIGH_GEAR_RATIO  = 24/50;
    
    private final double integratedEncCountsPerRev = 2048;

    private final double driveWheelRadius = 3;

    private boolean isDrvTrainInHighGear = true;    

    private AnalogInput pressureSensor;

    private final int PRESSURE_SENSOR_ANALOG_PORT = 3; 

    private final double PRESSURE_SENSOR_VOLTAGE_OFFSET = 0.5;

    private final double PRESSURE_SENSOR_VOLATGE_RANGE = 4.5; //4.5-0.5
    private final double MAX_PRESSURE = 200.0;

    private SupplyCurrentLimitConfiguration drvTrainCurrentLimit;

    private boolean enableCurrentLimit     = true; 
    private int currentLimitAmps           = 60;
    private int currentLimitTriggerAmps    = 80;
    private int currentLimitTimeoutSeconds = 5;

    private final int PID_IDX_CLOSED_LOOP = 0;
    private final int PID_TIMEOUT_MS      = 10;

    private final double DRIVE_STRAIGHT_PID_TUNING_CONSTANT = 0.945; //0.98;

    public final double PID_P = 0.05; // original value was 0.05
    public final double PID_I = 0.0001; // original value was 0.0005
    public final double PID_D = 0.1;   // original value was 0.1
    public final double PID_F = 0.02; // original value was 0.005    0.02 value for target speed 16000
    public final int IZONE    = 10;

    public double leftInitialEncoderPos;
    public double rightInitialEncoderPos;

    public boolean runningDistanceDrive = false;

    public double distanceGoal;
    public double distanceMoved;
    public final double STOP_THRESHOLD = 0.5;
    public final double SLOW_THRESHOLD = 30;

    public final double ENCODER_COUNTS_PER_INCH_LT = 1014.5; //without weight: 1046.6
    public final double ENCODER_COUNTS_PER_INCH_RT = 964; //without weight: 1025.7

    public final int DRIVE_DIST_MED_SPEED = 12000;
    public final int TURN_SPEED           = 12000;

    public final double TO_RADIANS = Math.PI/180;
    public final double TO_DEGREES = 180/Math.PI;

    public double currentEncPosition;
    public double turnRate;

    public double r1;
    public double r2;
    public double s1Dot;
    public double s2Dot;
    public double s1Conv;
    public double s2Conv;

    public Timer turnT;

    public CatzDriveTrain() 
    {
        drvTrainMtrCtrlLTFrnt = new WPI_TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlLTBack = new WPI_TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlRTFrnt = new WPI_TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlRTBack = new WPI_TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

        //Reset configuration for drivetrain MC's
        drvTrainMtrCtrlLTFrnt.configFactoryDefault();
        drvTrainMtrCtrlLTBack.configFactoryDefault();

        drvTrainMtrCtrlRTFrnt.configFactoryDefault();
        drvTrainMtrCtrlRTBack.configFactoryDefault();
        
        //Set current limit
        drvTrainCurrentLimit = new SupplyCurrentLimitConfiguration(enableCurrentLimit, currentLimitAmps, currentLimitTriggerAmps, currentLimitTimeoutSeconds);

        drvTrainMtrCtrlLTFrnt.configSupplyCurrentLimit(drvTrainCurrentLimit);
        drvTrainMtrCtrlLTBack.configSupplyCurrentLimit(drvTrainCurrentLimit);
        drvTrainMtrCtrlRTFrnt.configSupplyCurrentLimit(drvTrainCurrentLimit);
        drvTrainMtrCtrlRTBack.configSupplyCurrentLimit(drvTrainCurrentLimit);

        //Set back Motor Controllers to follow front Motor Controllers
        drvTrainMtrCtrlLTBack.follow(drvTrainMtrCtrlLTFrnt);
        drvTrainMtrCtrlRTBack.follow(drvTrainMtrCtrlRTFrnt);

        //Set MC's in brake mode
        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Brake);

        
        drvTrainLT = new SpeedControllerGroup(drvTrainMtrCtrlLTFrnt, drvTrainMtrCtrlLTBack);
        drvTrainRT = new SpeedControllerGroup(drvTrainMtrCtrlRTFrnt, drvTrainMtrCtrlRTBack);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PORT_A_PCM, DRVTRAIN_HGEAR_SOLENOID_PORT_B_PCM);

        pressureSensor = new AnalogInput(PRESSURE_SENSOR_ANALOG_PORT);

        turnT = new Timer();

        setDriveTrainPIDConfiguration();
    }

    public void setMotorsToCoast()
    {
        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Coast);
    }
    
    public void setMotorsToBrake()
    {
      
        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Brake);

    }

    public void setDriveTrainPIDConfiguration() 
    {
         //Configure feedback device for PID loop
         drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_IDX_CLOSED_LOOP, PID_TIMEOUT_MS); //Constants
         drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_IDX_CLOSED_LOOP, PID_TIMEOUT_MS);
 
         //Configure PID Gain Constants
         drvTrainMtrCtrlLTFrnt.config_kP(0, PID_P);
         drvTrainMtrCtrlLTFrnt.config_kI(0, PID_I);
         drvTrainMtrCtrlLTFrnt.config_kD(0, PID_D);
         drvTrainMtrCtrlLTFrnt.config_kF(0, PID_F);
         drvTrainMtrCtrlLTFrnt.config_IntegralZone(0, IZONE);

         drvTrainMtrCtrlRTFrnt.config_kP(0, PID_P);
         drvTrainMtrCtrlRTFrnt.config_kI(0, PID_I);
         drvTrainMtrCtrlRTFrnt.config_kD(0, PID_D);
         drvTrainMtrCtrlRTFrnt.config_kF(0, PID_F);
         drvTrainMtrCtrlRTFrnt.config_IntegralZone(0, IZONE); 
 
        /* drvTrainMtrCtrlRTFrnt.config_kP(0, 0.03);
         drvTrainMtrCtrlRTFrnt.config_kI(0, 0.0006);
         drvTrainMtrCtrlRTFrnt.config_kD(0, 0.1);
         drvTrainMtrCtrlRTFrnt.config_kF(0, 0.005);
         drvTrainMtrCtrlRTFrnt.config_IntegralZone(0, 0); */
    }

    public void monitorEncoderPositionTurn()
    {
        if (runningDistanceDrive == true)
        {
            currentEncPosition = getIntegratedEncPosition("LT");
            distanceMoved = leftEncoderDistanceMoved(currentEncPosition);

            
            SmartDashboard.putNumber("Encoder Position", currentEncPosition);
            SmartDashboard.putNumber("Initial Encoder Distance", leftInitialEncoderPos);
            SmartDashboard.putNumber("Distance Moved", distanceMoved);
            SmartDashboard.putNumber("Delta Encoder", (currentEncPosition - leftInitialEncoderPos));

            //System.out.println((currentEncPosition - leftInitialEncoderPos) + " = " + currentEncPosition + " - " + leftInitialEncoderPos);

            double distanceToGoal = distanceGoal - distanceMoved;

            SmartDashboard.putNumber("Distance To Goal", distanceToGoal);

            if (distanceToGoal < STOP_THRESHOLD)
            {
                setTargetVelocity(0);
                runningDistanceDrive = false;
            }

            else if (distanceToGoal < SLOW_THRESHOLD)
            {
                System.out.println("Starting slow: " + (getIntegratedEncVelocity("LT")*0.95));
                setTargetVelocity(getIntegratedEncVelocity("LT")*0.98);
                
            }
        }
    }

    public void monitorEncoderPosition()
    {
        if (runningDistanceDrive == true)
        {
            currentEncPosition = getIntegratedEncPosition("LT");
            distanceMoved = leftEncoderDistanceMoved(currentEncPosition);

            
            SmartDashboard.putNumber("Encoder Position", currentEncPosition);
            SmartDashboard.putNumber("Initial Encoder Distance", leftInitialEncoderPos);
            SmartDashboard.putNumber("Distance Moved", distanceMoved);
            SmartDashboard.putNumber("Delta Encoder", (currentEncPosition - leftInitialEncoderPos));

            //System.out.println((currentEncPosition - leftInitialEncoderPos) + " = " + currentEncPosition + " - " + leftInitialEncoderPos);

            double distanceToGoal = distanceGoal - distanceMoved;

            SmartDashboard.putNumber("Distance To Goal", distanceToGoal);

            if (distanceToGoal < STOP_THRESHOLD)
            {
                setTargetVelocity(0);
                runningDistanceDrive = false;
            }

            else if (distanceToGoal < SLOW_THRESHOLD)
            {
                System.out.println("Starting slow: " + (getIntegratedEncVelocity("LT")*0.95));
                setTargetVelocity(getIntegratedEncVelocity("LT")*0.98);
                
            }
        }
    }

    public void setDistanceGoal(double inches)
    {
        if(!runningDistanceDrive)
        {
            distanceGoal = inches;
            runningDistanceDrive = true;
            leftInitialEncoderPos = drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(0);
            setTargetVelocity(DRIVE_DIST_MED_SPEED);
        }
    }

    public void setDistanceGoalTurn(double leftVelocity, double rightVelocity)
    {
        if(Robot.navx.getAngle() < 90)
        {
            drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, leftVelocity);
            drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -rightVelocity);

            drvTrainMtrCtrlLTBack.follow(drvTrainMtrCtrlLTFrnt);
            drvTrainMtrCtrlRTBack.follow(drvTrainMtrCtrlRTFrnt);
            turnT.reset();
            turnT.start();
        }
        double deltaTime = 0;
        double startTime = turnT.get();
        while(Robot.navx.getAngle() < 90) 
        {
            deltaTime = turnT.get() - startTime;
            SmartDashboard.putNumber("deltaTime", deltaTime);
        }

        setTargetVelocity(0);

        
    }

    public void radialTurn(double radiusOfCurvature, double turnRateDegrees, double targetAngleDegrees)
    {
        turnT.reset();

        r1 = radiusOfCurvature;
        r2 = radiusOfCurvature + (7.0/3.0);

        turnRate = turnRateDegrees*(Math.PI/180);

        s1Dot = r1 * turnRate;
        s2Dot = r2 * turnRate;

        s1Conv = s1Dot * ENCODER_COUNTS_PER_INCH_RT * 12 *(1.0/10.0);
        s2Conv = s2Dot * ENCODER_COUNTS_PER_INCH_LT * 12 *(1.0/10.0);

        SmartDashboard.putNumber("s1", s1Dot);
        SmartDashboard.putNumber("s2", s2Dot);
        SmartDashboard.putNumber("s1Conv", s1Conv);
        SmartDashboard.putNumber("s2Conv", s2Conv);
        SmartDashboard.putNumber("TargetAngle", targetAngleDegrees);
        SmartDashboard.putNumber("TurnRateDegrees", turnRateDegrees);

        double targetAngle = targetAngleDegrees * (Math.PI / 180);
        double timeOut = (targetAngle)/turnRate;
       
        turnT.start();

        SmartDashboard.putNumber("Time Out", timeOut);

        double deltaTime;
        double timeStart = turnT.get();

        while ((turnT.get() - timeStart) < 2)
        {
            drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, s2Conv);
            drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -s1Conv * DRIVE_STRAIGHT_PID_TUNING_CONSTANT);

            drvTrainMtrCtrlLTBack.follow(drvTrainMtrCtrlLTFrnt);
            drvTrainMtrCtrlRTBack.follow(drvTrainMtrCtrlRTFrnt);

            deltaTime = turnT.get() - timeStart;
            SmartDashboard.putNumber("Delta time", deltaTime);
        }
        if(turnT.get() > timeOut)
        {
            drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, 0);
            drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, 0);
        }

    }

    public double leftEncoderDistanceMoved(double encoderPosition)
    {  
        //System.out.println((getIntegratedEncPosition("LT") - leftInitialEncoderPos) + " = " + getIntegratedEncPosition("LT") + " - " + leftInitialEncoderPos);
        return (encoderPosition - leftInitialEncoderPos) / ENCODER_COUNTS_PER_INCH_LT;
    }

    public double rightEncoderDistanceMoved()
    {   
        rightInitialEncoderPos = drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
        return (getIntegratedEncPosition("RT") - rightInitialEncoderPos) * ENCODER_COUNTS_PER_INCH_RT;
    }


    public void arcadeDrive(double power, double rotation)
    {
       drvTrainDifferentialDrive.arcadeDrive(-power, rotation);
    }

    public void shiftToHighGear()
    {
        gearShifter.set(Value.kForward);
        isDrvTrainInHighGear = true;
    }
    
    public void shiftToLowGear()
    {
        gearShifter.set(Value.kReverse);
        isDrvTrainInHighGear = false;
    }

    public double getMotorTemperature(int id)
    {
        double temp = 0.0;
        if(id == DRVTRAIN_LT_FRNT_MC_CAN_ID)
        {
            temp = drvTrainMtrCtrlLTFrnt.getTemperature();
        } 
        else if (id == DRVTRAIN_LT_BACK_MC_CAN_ID)
        {   
            temp = drvTrainMtrCtrlLTBack.getTemperature();
        }
        else if (id == DRVTRAIN_RT_FRNT_MC_CAN_ID)
        {
            temp = drvTrainMtrCtrlRTFrnt.getTemperature();
        }
        else if (id == DRVTRAIN_RT_BACK_MC_CAN_ID)
        {
            temp = drvTrainMtrCtrlRTBack.getTemperature();
        }
        return temp;
    }




    public double getSrxMagPosition(String side)
    {
        side.toUpperCase();
        double position = 0.0;
        if(side.equals("LT"))
        {
            //position = Robot.climber.climbMtrCtrlA.getEncoder().getPosition(); //LT encoder is connnected to climber MC A
        }
        else if(side.equals("RT"))
        {
            //position = Robot.climber.climbMtrCtrlB.getEncoder().getPosition(); //RT encoder is connected to climber MC B
        }
        return position;
    }

    public double getIntegratedEncPosition(String side) 
    {
        double position = 0.0;
        side.toUpperCase();
        if(side.equals("LT"))
        {
            position = drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(0);
        }
        else if(side.equals("RT"))
        {
            position = drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
        }
        return position;
    }
    
    public double getIntegratedEncVelocity(String side)
    {
        double velocity = 0.0;
        side.toUpperCase();
        if(side.equals("LT"))
        {
            velocity = drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
        }
        else if(side.equals("RT"))
        {
            velocity = drvTrainMtrCtrlRTFrnt.getSensorCollection().getIntegratedSensorVelocity();
        }
        return velocity;
    }

    public void setTargetPosition(double targetPosition)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Position, targetPosition);
        drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Position, targetPosition);
    }
    
    public void setTargetVelocity(double targetVelocity)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, targetVelocity);
        drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -DRIVE_STRAIGHT_PID_TUNING_CONSTANT*targetVelocity);
        drvTrainMtrCtrlLTBack.follow(drvTrainMtrCtrlLTFrnt);
        drvTrainMtrCtrlRTBack.follow(drvTrainMtrCtrlRTFrnt);
    }

    public void setIntegratedEncPosition(int position)
    {
        drvTrainMtrCtrlLTFrnt.setSelectedSensorPosition(position);
    }

    public double convertLinearVelocityToAngularVelcoity(double linearVelocity)
    {
        return linearVelocity*12.0/driveWheelRadius/(2*Math.PI)*integratedEncCountsPerRev/1000.0*100.0;
    }

    public double getPSI(double voltage)
    {
      voltage = pressureSensor.getVoltage() - PRESSURE_SENSOR_VOLTAGE_OFFSET;
      return (MAX_PRESSURE/PRESSURE_SENSOR_VOLATGE_RANGE) *voltage;  
    }   

    public void instantiateDifferentialDrive()
    {
        drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);
    }
}