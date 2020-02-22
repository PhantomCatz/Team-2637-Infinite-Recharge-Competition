package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CatzDriveTrain
{
    public static WPI_TalonFX drvTrainMtrCtrlLTFrnt; //Private
    public static WPI_TalonFX drvTrainMtrCtrlLTBack;

    public static WPI_TalonFX drvTrainMtrCtrlRTFrnt;
    public static WPI_TalonFX drvTrainMtrCtrlRTBack;

    private static WPI_TalonSRX srxEncLT;
    private static WPI_TalonSRX srxEncRT;

    private final int DRVTRAIN_LT_FRNT_MC_CAN_ID = 1;
    private final int DRVTRAIN_LT_BACK_MC_CAN_ID = 2;

    private final int DRVTRAIN_RT_FRNT_MC_CAN_ID = 3;
    private final int DRVTRAIN_RT_BACK_MC_CAN_ID = 4;

    private static DifferentialDrive drvTrainDifferentialDrive;

    private static SpeedControllerGroup drvTrainLT;
    private static SpeedControllerGroup drvTrainRT;

    private static DoubleSolenoid gearShifter;

    private final int DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A = 0; //Solenoid Port A PCM
    private final int DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B = 1;

    private final double gearRatio     = 11/44; //Make values as constants
    private final double lowGearRatio  = 14/60;
    private final double highGearRatio = 24/50;
    
    private final double integratedEncCountsPerRev = 2048;
    private final double driveWheelRadius = 3;

    private boolean isDrvTrainInLowGear = false;    //boolean high = true;

    private AnalogInput pressureSensor;

    private final int PRESSURE_SENSOR_PORT_A = 3; //Change the name

    private final double PRESSURE_SENSOR_VOLTAGE_OFFSET = 0.5;

    private final double    PRESSURE_SENSOR_VOLATGE_RANGE = 4.0; //4.5-0.5
    private final double MAX_PRESSURE = 200;

    private SupplyCurrentLimitConfiguration drvTrainCurrentLimit;

    public boolean enableCurrentLimit = true;  //private
    public int currentLimitAmps = 60;
    public int currentLimitTriggerAmps = 80;
    public int currentLimitTimeoutSeconds = 5;


    public CatzDriveTrain() 
    {
        srxEncLT = new WPI_TalonSRX(5);
        srxEncRT = new WPI_TalonSRX(6);

        drvTrainMtrCtrlLTFrnt = new WPI_TalonFX(DRVTRAIN_LT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlLTBack = new WPI_TalonFX(DRVTRAIN_LT_BACK_MC_CAN_ID);

        drvTrainMtrCtrlRTFrnt = new WPI_TalonFX(DRVTRAIN_RT_FRNT_MC_CAN_ID);
        drvTrainMtrCtrlRTBack = new WPI_TalonFX(DRVTRAIN_RT_BACK_MC_CAN_ID);

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

        drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        //Configure feedback device for PID loop
        drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100); //Constants
        drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);

      //  drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,1,100); //delete it
      //  drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,1,100);

        //Configure PID Gain Constants
        drvTrainMtrCtrlLTFrnt.config_kP(0, 0.05);
        drvTrainMtrCtrlLTFrnt.config_kI(0, 0.0005);
        drvTrainMtrCtrlLTFrnt.config_kD(0, 0.1);
        drvTrainMtrCtrlLTFrnt.config_kF(0, 0.005);
        drvTrainMtrCtrlLTFrnt.config_IntegralZone(0, 0);


        drvTrainMtrCtrlRTFrnt.config_kP(0, 0.03);
        drvTrainMtrCtrlRTFrnt.config_kI(0, 0.0006);
        drvTrainMtrCtrlRTFrnt.config_kD(0, 0.1);
        drvTrainMtrCtrlRTFrnt.config_kF(0, 0.005);
        drvTrainMtrCtrlRTFrnt.config_IntegralZone(0, 0); 

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PCM_PORT_A, DRVTRAIN_HGEAR_SOLENOID_PCM_PORT_B);

        pressureSensor = new AnalogInput(PRESSURE_SENSOR_PORT_A);

    }

    public void arcadeDrive(double power, double rotation)
    {
        drvTrainDifferentialDrive.arcadeDrive(power, rotation);
    }

    public void deployGearShift()  //shift to high or low
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

    public double getSrxMagLTPosition() //Combine into one method
    {
       return srxEncLT.getSensorCollection().getQuadraturePosition();
    }

    public double getSrxMagRTPosition()
    {
        return srxEncRT.getSensorCollection().getQuadraturePosition();
    }

    public double getIntegratedEncLTPosition() //combine into one method
    {
        return drvTrainMtrCtrlLTFrnt.getSelectedSensorPosition(0);
    }

    public double getIntegratedEncRTPosition()
    {
        return drvTrainMtrCtrlRTFrnt.getSelectedSensorPosition(0);
    }
    
    public double getIntegratedEncLTVelocity()
    {
        return drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
    }
    public double getIntegratedEncRTVelocity()
    {
        return drvTrainMtrCtrlRTFrnt.getSensorCollection().getIntegratedSensorVelocity();
    }
   
    public double getLTEncLinearVelocity() //Change the method
    {
        double linearVelocity = drvTrainMtrCtrlLTFrnt.getSensorCollection().getIntegratedSensorVelocity();
        linearVelocity = (linearVelocity/0.1)/integratedEncCountsPerRev;
        linearVelocity = (linearVelocity/gearRatio)/lowGearRatio;
        linearVelocity = (linearVelocity*2*Math.PI)*driveWheelRadius; 
        return linearVelocity*12; 
    }

    public void setTargetPosition(double targetPosition)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Position, targetPosition);
        drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Position, targetPosition);
    }
    
    public void setTargetVelocity(double targetVelocity)
    {
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, targetVelocity);
        drvTrainMtrCtrlRTFrnt.set(TalonFXControlMode.Velocity, -targetVelocity);
    }

    public void setIntegratedEncPosition(int position)
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
        encoderVelocity = encoderVelocity * integratedEncCountsPerRev;
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
        SmartDashboard.putNumber("converted vel", encoderVelocity);
        drvTrainMtrCtrlLTFrnt.set(TalonFXControlMode.Velocity, encoderVelocity);
    }

    public double getPSI(double voltage)
    {
      voltage = pressureSensor.getVoltage() - PRESSURE_SENSOR_VOLTAGE_OFFSET;
      return (MAX_PRESSURE/PRESSURE_SENSOR_VOLATGE_RANGE) *voltage;  
     }
    
}
