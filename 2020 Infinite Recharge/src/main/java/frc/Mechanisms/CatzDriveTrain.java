package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
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

<<<<<<< Updated upstream
    final int length = 3;
    double[] currentLimitConfigArray = new double[length];
=======
    private static DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PORT_A_PCM = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PORT_B_PCM = 1;

    private final double gearRatio     = 11/44;
    private final double lowGearRatio  = 14/60;
    private final double highGearRatio = 24/50;
    
    private final double integratedEncCountsPerRev = 2048;

    private final double driveWheelRadius = 3;

    public static boolean isDrvTrainInHighGear = true;    //boolean high = true;
>>>>>>> Stashed changes

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

        drvTrainMtrCtrlLTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlLTBack.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTFrnt.setNeutralMode(NeutralMode.Coast);
        drvTrainMtrCtrlRTBack.setNeutralMode(NeutralMode.Coast);

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
<<<<<<< Updated upstream
=======
        isDrvTrainInHighGear = false;
    }

    public String getDriveTrainGearMode()
    {
        if (isDrvTrainInHighGear)
            return "High";
        else
            return "Low";
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

    public double getSrxMagPosition(String side) //Combine into one method
    {
        side.toUpperCase();
        double position = 0.0;
        if(side.equals("LT"))
        {
            position = srxEncLT.getSensorCollection().getQuadraturePosition();
        }
        else if(side.equals("RT"))
        {
            position = srxEncRT.getSensorCollection().getQuadraturePosition();
        }
        return position;
    }

    public double getIntegratedEncLTPosition(String side) //combine into one method
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
    
    public double getIntegratedEncLTVelocity(String side)
    {
        double velocity = 0.0;
        side.toUpperCase();
        if(side.equals("LT"))
        {
            velocity = drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
        }
        else if(side.equals("RT"))
        {
            velocity = drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
        }
        return velocity;
>>>>>>> Stashed changes
    }
}    