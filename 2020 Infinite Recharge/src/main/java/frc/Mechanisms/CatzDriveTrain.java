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
    private WPI_TalonFX drvTrainMtrCtrlLTFrnt;
    private WPI_TalonFX drvTrainMtrCtrlLTBack;
    private WPI_TalonFX drvTrainMtrCtrlRTFrnt;
    private WPI_TalonFX drvTrainMtrCtrlRTBack;

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

    private final int DRVTRAIN_LGEAR_SOLENOID_PORT_A_PCM = 0;
    private final int DRVTRAIN_HGEAR_SOLENOID_PORT_B_PCM = 1;

    private final double gearRatio     = 11/44;
    private final double lowGearRatio  = 14/60;
    private final double highGearRatio = 24/50;
    
    private final double integratedEncCountsPerRev = 2048;

    private final double driveWheelRadius = 3;

    private boolean isDrvTrainInHighGear = true;    //boolean high = true;

    private AnalogInput pressureSensor;

    private final int PRESSURE_SENSOR_ANALOG_PORT = 3; 

    private final double PRESSURE_SENSOR_VOLTAGE_OFFSET = 0.5;

    private final double PRESSURE_SENSOR_VOLATGE_RANGE = 4.5; //4.5-0.5
    private final double MAX_PRESSURE = 200;

    private SupplyCurrentLimitConfiguration drvTrainCurrentLimit;

    private boolean enableCurrentLimit = true; 
    private int currentLimitAmps = 60;
    private int currentLimitTriggerAmps = 80;
    private int currentLimitTimeoutSeconds = 5;

    private final int PID_IDX_CLOSED_LOOP = 0;
    private final int PID_TIMEOUT_MS = 10;


    public CatzDriveTrain() 
    {
        srxEncLT = new WPI_TalonSRX(5);
        srxEncRT = new WPI_TalonSRX(6);

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

        drvTrainDifferentialDrive = new DifferentialDrive(drvTrainLT, drvTrainRT);

        gearShifter = new DoubleSolenoid(DRVTRAIN_LGEAR_SOLENOID_PORT_A_PCM, DRVTRAIN_HGEAR_SOLENOID_PORT_B_PCM);

        pressureSensor = new AnalogInput(PRESSURE_SENSOR_ANALOG_PORT);

        setDriveTrainPIDConfiguration();
    }

    public void setDriveTrainPIDConfiguration() 
    {
         //Configure feedback device for PID loop
         drvTrainMtrCtrlLTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_IDX_CLOSED_LOOP, PID_TIMEOUT_MS); //Constants
         drvTrainMtrCtrlRTFrnt.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_IDX_CLOSED_LOOP, PID_TIMEOUT_MS);
 
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
    }

    public void arcadeDrive(double power, double rotation)
    {
        drvTrainDifferentialDrive.arcadeDrive(power, rotation);
    }

    public void shiftToHighGear()
    {
        gearShifter.set(Value.kReverse);
        isDrvTrainInHighGear = true;
    }
    
    public void shiftToLowGear()
    {
        gearShifter.set(Value.kForward);
        isDrvTrainInHighGear = false;
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

    public double getPSI(double voltage)
    {
      voltage = pressureSensor.getVoltage() - PRESSURE_SENSOR_VOLTAGE_OFFSET;
      return (MAX_PRESSURE/PRESSURE_SENSOR_VOLATGE_RANGE) *voltage;  
     }
    
}
