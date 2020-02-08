package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

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

    private static WPI_TalonSRX encoderLT;
    private static WPI_TalonSRX encoderRT;

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

    public CatzDriveTrain() 
    {
        drvTrainMtrCtrlLTFrnt = new WPI_TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlLTBack = new WPI_TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        //drvTrainMtrCtrlLTBack.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
        //0 = primary loop
        //100 = timeout in ms

        drvTrainMtrCtrlRTFrnt = new WPI_TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlRTBack = new WPI_TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

        encoderLT = new WPI_TalonSRX(5);
        encoderRT = new WPI_TalonSRX(6);

        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Brake);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Brake);

        /*
        currentLimitConfigArray[enabledStatusIndex] = enabled;
        currentLimitConfigArray[currentLimitIndex] = currentLimit;
        currentLimitConfigArray[triggerThresholdCurrentIndex] = triggerThresholdCurrent;
        currentLimitConfigArray[triggerThresholdTimeIndex] = triggerThresholdTime; 
        StatorCurrentLimitConfiguration s = new StatorCurrentLimitConfiguration(currentLimitConfigArray);
        drvTrainMtrCtrlLTFrnt.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlLTBack.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlRTFrnt.configGetStatorCurrentLimit(s);
        drvTrainMtrCtrlRTBack.configGetStatorCurrentLimit(s);*/

        drvTrainLT = new SpeedControllerGroup(drvTrainMtrCtrlLTFrnt, drvTrainMtrCtrlLTBack);
        drvTrainRT = new SpeedControllerGroup(drvTrainMtrCtrlRTFrnt, drvTrainMtrCtrlRTBack);

        drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A, DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B);

        drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,100);
        drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,100);

        drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,1,100);
        drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,1,100);
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

    public double getMotorTemperature(int id)
    {
        double temp = 0.0;

        if(id == 1)
        {
            temp = drvTrainMtrCtrlLTFrnt.getTemperature();
        } 
        else if (id == 2)
        
        {   
            temp = drvTrainMtrCtrlLTBack.getTemperature();
        }
        else if (id == 3)
        {
            temp = drvTrainMtrCtrlRTFrnt.getTemperature();
        }
        else if (id == 4)
        {
            temp = drvTrainMtrCtrlRTBack.getTemperature();
        }

        return temp;

    }

    public double getSrxMagLT() 
    {
       return encoderLT.getSensorCollection().getQuadraturePosition();
        // return drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(1);
    }

    public double getSrxMagRT()
    {
        return encoderRT.getSensorCollection().getQuadraturePosition();
       // return drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(1);
    }

    public double getInternaEncoderLT()
    {
        return drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(0);
    }

    public double getInternaEncoderRT()
    {
        return drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
    }

    
}
