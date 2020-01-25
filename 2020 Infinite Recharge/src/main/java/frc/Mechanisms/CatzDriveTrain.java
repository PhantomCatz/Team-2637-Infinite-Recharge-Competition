package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class CatzDriveTrain
{
    private static WPI_TalonFX drvTrainMtrCtrlLTFrnt;
    private static WPI_TalonFX drvTrainMtrCtrlLTBack;

    private static WPI_TalonFX drvTrainMtrCtrlRTFrnt;
    private static WPI_TalonFX drvTrainMtrCtrlRTBack;

    private final int DRVTRAIN_LT_FRNT_MC_CAN_ID = 1;
    private final int DRVTRAIN_LT_BACK_MC_CAN_ID = 2;

    private final int DRVTRAIN_RT_FRNT_MC_CAN_ID = 3;
    private final int DRVTRAIN_RT_BACK_MC_CAN_ID = 4;

    private static DifferentialDrive drvTrainDifferentialDrive;

    private static SpeedControllerGroup drvTrainLT;
    private static SpeedControllerGroup drvTrainRT;

    /* current limiting values
    final int length = 3;
    double[] currentLimitConfigArray = new double[length];

    final int enabledStatusIndex = 0;
    final int currentLimitIndex = 1;
    final int triggerThresholdCurrentIndex = 2;
    final int triggerThresholdTimeIndex = 3; 

    int enabled = 1;
    double currentLimit = 60;
    double triggerThresholdCurrent = 60;
    double triggerThresholdTime = 5;*/

    private static DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B = 1;

    private final double gearRatio     = 11/44;
    private final double lowGearRatio  = 14/60;
    private final double highGearRatio = 24/50;
    
    private final double integradedEncCountsPerRev = 2048;
    private final double driveWheelRadius = 3;

    private boolean isDrvTrainInLowGear = false;

    public CatzDriveTrain() 
    {
        drvTrainMtrCtrlLTFrnt = new WPI_TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlLTBack = new WPI_TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlRTFrnt = new WPI_TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlRTBack = new WPI_TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

        /**
         *  Set back Motor Controllers to follow front Motor Controllers
         */
        drvTrainMtrCtrlLTBack.follow(drvTrainMtrCtrlLTFrnt);
        drvTrainMtrCtrlRTBack.follow(drvTrainMtrCtrlRTFrnt);

        /**
         *  Configure feedback device for PID loop
         */
        drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);
        drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);
        //0 = primary loop
        //100 = timeout in ms
        
        /**
         *  Set MC's in coast mode
         */
        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Coast);
        

        /**
         *  Configure PID Gain Constants
         */
        drvTrainMtrCtrlLTFrnt.config_kP(0, 0.3);
        drvTrainMtrCtrlLTFrnt.config_kI(0, 0.0);
        drvTrainMtrCtrlLTFrnt.config_kD(0, 0.0);
        drvTrainMtrCtrlLTFrnt.config_kF(0, 0.0);
        drvTrainMtrCtrlLTFrnt.config_IntegralZone(0, 0);

        drvTrainMtrCtrlRTFrnt.config_kP(0, 0.3);
        drvTrainMtrCtrlRTFrnt.config_kI(0, 0.0);
        drvTrainMtrCtrlRTFrnt.config_kD(0, 0.0);
        drvTrainMtrCtrlRTFrnt.config_kF(0, 0.0);
        drvTrainMtrCtrlRTFrnt.config_IntegralZone(0, 0);

        /**
         *  Configure current limiting on MC's
         *     Doesnt work yet, gives an error
         */
        /*currentLimitConfigArray[enabledStatusIndex] = enabled;
        currentLimitConfigArray[currentLimitIndex] = currentLimit;
        currentLimitConfigArray[triggerThresholdCurrentIndex] = triggerThresholdCurrent;
        currentLimitConfigArray[triggerThresholdTimeIndex] = triggerThresholdTime; 
        StatorCurrentLimitConfiguration s = new StatorCurrentLimitConfiguration(currentLimitConfigArray);*/
        
        //drvTrainMtrCtrlLTFrnt.configNominalOutputForward(percentOut);
        //drvTrainMtrCtrlLTFrnt.configNominalOutputReverse(percentOut);
        //drvTrainMtrCtrlLTFrnt.configPeakOutputForward(percentOut);
        //drvTrainMtrCtrlLTFrnt.configPeakOutputReverse(percentOut;)

        drvTrainLT = new SpeedControllerGroup(drvTrainMtrCtrlLTFrnt, drvTrainMtrCtrlLTBack);
        drvTrainRT = new SpeedControllerGroup(drvTrainMtrCtrlRTFrnt, drvTrainMtrCtrlRTBack);

        //drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A, DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B);
    }

    public void arcadeDrive(double power, double rotation)
    {
        drvTrainDifferentialDrive.arcadeDrive(power, rotation);
    }
    
    public void deployGearShift()
    {
        gearShifter.set(Value.kReverse);
    }
    public void retractGearShift()
    {
        gearShifter.set(Value.kForward);
    }
    
    public double getLTEncPosition()
    {
        return drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorPosition();
    }
    public double getLTEncVelocity()
    {
        return drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
    }

    public double getRTEncPostion()
    {
        return drvTrainMtrCtrlRTFrnt.getSensorCollection().getIntegratedSensorPosition();
    }
    public double getRTEncVelocity()
    {
        return drvTrainMtrCtrlRTFrnt.getSensorCollection().getIntegratedSensorVelocity();
    }

    public void setTargetPosition(double targetPosition)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Position, targetPosition);
    }
    
    public void setTargetVelocity(double targetVelocity)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, targetVelocity);
    }

    public void setEncPosition(int position)
    {
        drvTrainMtrCtrlLTFrnt.setSelectedSensorPosition(position);
    }

    public void leftGearboxVelocityPID(double targetVelocity)
    {
        //asume target velocity is in ft/s,
        //need to change units in to counts/100ms
        
        //convert to in/s
        double encoderVelocity = targetVelocity*12;
        //convert to rad/s
        encoderVelocity = encoderVelocity / driveWheelRadius;
        //convert to rev/s
        encoderVelocity = encoderVelocity / (2*Math.PI);
        //convert to counts/s
        encoderVelocity = encoderVelocity * integradedEncCountsPerRev;
        //convert to counts/100ms
        encoderVelocity = encoderVelocity * 0.1;

        if(isDrvTrainInLowGear)
        {
            encoderVelocity = encoderVelocity * lowGearRatio * gearRatio;
        }
        else //drive train is in high gear
        {
            encoderVelocity = encoderVelocity * highGearRatio * gearRatio;
        }
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, encoderVelocity);
    }
}    