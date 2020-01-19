package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class CatzDriveTrain {
    private static TalonFX drvTrainMtrCtrlLTFrnt;
    private static TalonFX drvTrainMtrCtrlLTBack;

    private static TalonFX drvTrainMtrCtrlRTFrnt;
    private static TalonFX drvTrainMtrCtrlRTBack;

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
//helloooooo?
    private static DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B = 1;

    public CatzDriveTrain() 
    {
        drvTrainMtrCtrlLTFrnt = new TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlLTBack = new TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        SpeedControllerGroup drvTrainLT = new SpeedControllerGroup((SpeedController) drvTrainMtrCtrlLTFrnt, (SpeedController) drvTrainMtrCtrlLTBack);
        SpeedControllerGroup drvTrainRT = new SpeedControllerGroup((SpeedController) drvTrainMtrCtrlRTFrnt, (SpeedController) drvTrainMtrCtrlRTBack);

        drvTrainMtrCtrlRTFrnt = new TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlRTBack = new TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Coast);

        
        currentLimitConfigArray[enabledStatusIndex] = enabled;
        currentLimitConfigArray[currentLimitIndex] = currentLimit;
        currentLimitConfigArray[triggerThresholdCurrentIndex] = triggerThresholdCurrent;
        currentLimitConfigArray[triggerThresholdTimeIndex] = triggerThresholdTime; 

        StatorCurrentLimitConfiguration s = new StatorCurrentLimitConfiguration(currentLimitConfigArray);

        drvTrainMtrCtrlLTFrnt.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlLTBack.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlRTFrnt.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlRTBack.configGetStatorCurrentLimit(s);

        drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A, DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B);
    }
    
    public static void setDrvTrainLT(SpeedControllerGroup drvTrainLT) 
    {
        CatzDriveTrain.drvTrainLT = drvTrainLT;
    }

    public void arcadeDrive(final double power, final double rotation)
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
}    