package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    final int length = 3;
    double[] currentLimitConfigArray = new double[length];

    final int enabledStatusIndex = 0;
    final int currentLimitIndex = 1;
    final int triggerThresholdCurrentIndex = 2;
    final int triggerThresholdTimeIndex = 3; 

    int enabled = 1;
    double currentLimit = 60;
    double triggerThresholdCurrent = 60;
    double triggerThresholdTime = 5;

    private static DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B = 1;
    public double kP, kI, kD, kF;

    public CatzDriveTrain() 
    {
        drvTrainMtrCtrlLTFrnt = new WPI_TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        //drvTrainMtrCtrlLTBack = new WPI_TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);
        //0 = primary loop
        //100 = timeout in ms
       
    
     
        
        drvTrainMtrCtrlLTFrnt.config_kP(0, 0.04);
        drvTrainMtrCtrlLTFrnt.config_kI(0, 0.0);
        drvTrainMtrCtrlLTFrnt.config_kD(0, 1);
        drvTrainMtrCtrlLTFrnt.config_kF(0, 1);
        drvTrainMtrCtrlLTFrnt.config_IntegralZone(0, 0);

        SmartDashboard.putNumber("P Gain", kP);

        SmartDashboard.putNumber("I Gain", kI);
    
        SmartDashboard.putNumber("D Gain", kD);
    
        SmartDashboard.putNumber("Feed Forward", kF);

        

           
        
        
        drvTrainMtrCtrlRTFrnt = new WPI_TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        //drvTrainMtrCtrlRTBack = new WPI_TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Coast);
        //drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Coast);
        //drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Coast);
        
        /*currentLimitConfigArray[enabledStatusIndex] = enabled;
        currentLimitConfigArray[currentLimitIndex] = currentLimit;
        currentLimitConfigArray[triggerThresholdCurrentIndex] = triggerThresholdCurrent;
        currentLimitConfigArray[triggerThresholdTimeIndex] = triggerThresholdTime; 

        StatorCurrentLimitConfiguration s = new StatorCurrentLimitConfiguration(currentLimitConfigArray);*/

        //drvTrainMtrCtrlLTFrnt.configGetStatorCurrentLimit(s);
        //drvTrainMtrCtrlLTBack.configGetStatorCurrentLimit(s);
        //drvTrainMtrCtrlRTFrnt.configGetStatorCurrentLimit(s);
        //drvTrainMtrCtrlRTBack.configGetStatorCurrentLimit(s);

        drvTrainLT = new SpeedControllerGroup(drvTrainMtrCtrlLTFrnt);//, drvTrainMtrCtrlLTBack);
        drvTrainRT = new SpeedControllerGroup(drvTrainMtrCtrlRTFrnt);//, drvTrainMtrCtrlRTBack);

        //drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A, DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B);
    }

    public void arcadeDrive(final double power, final double rotation) {
        drvTrainDifferentialDrive.arcadeDrive(power, rotation);
    }

    public void setDriveKp(int slotIdx, double kpValue) {
        drvTrainMtrCtrlLTFrnt.config_kP(slotIdx, kpValue);
    }
    public void setDriveKI(int slotIdx, double kiValue) {
        drvTrainMtrCtrlLTFrnt.config_kI(slotIdx, kiValue);
    }
    public void setDriveKD(int slotIdx, double kdValue) {
        drvTrainMtrCtrlLTFrnt.config_kD(slotIdx, kdValue);
    }
    public void setDriveKF(int slotIdx, double kfValue) {
        drvTrainMtrCtrlLTFrnt.config_kF(slotIdx, kfValue);
    }


    public void deployGearShift() {
        gearShifter.set(Value.kReverse);
    }

    public void retractGearShift() {
        gearShifter.set(Value.kForward);
    }

    public double getLTEncPosition() {
        return drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorPosition();

    }

    public double getLTEncVelocity() {
        return drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
    }

    public double getRTEncPostion() {
        return drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
    }

    public double getRTEncVelocity() {
        return drvTrainMtrCtrlRTFrnt.getSelectedSensorVelocity(0);
    }

    public void setTargetPosition(final double targetPosition) {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Position, targetPosition);
    }

    public void setTargetVelocity(final double targetVelocity) {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, targetVelocity);
    }

    public void setEncPosition(final int position)
    {
        drvTrainMtrCtrlLTFrnt.setSelectedSensorPosition(position);
    }
}    