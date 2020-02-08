package frc.Mechanisms;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class CatzClimber
{
    private CANSparkMax climbMtrCtrlA; 
    private CANSparkMax climbMtrCtrlB;

    private WPI_VictorSPX lightsaber;

    private int CLIMB_MTR_CTRL_A_CAN_ID = 30;
    private int CLIMB_MTR_CTRL_B_CAN_ID = 31;
    private int LIGHTSABER_MTR_CTRL_CAN_ID = 50;

    final private int currentLimit = 80;

    //private static SpeedControllerGroup ClimberSpeedController;
    
    public CatzClimber()
    {
        climbMtrCtrlA = new CANSparkMax(CLIMB_MTR_CTRL_A_CAN_ID, MotorType.kBrushless);
        climbMtrCtrlB = new CANSparkMax(CLIMB_MTR_CTRL_B_CAN_ID, MotorType.kBrushless);

        lightsaber = new WPI_VictorSPX(LIGHTSABER_MTR_CTRL_CAN_ID);

        //reset config
        climbMtrCtrlA.restoreFactoryDefaults();
        climbMtrCtrlB.restoreFactoryDefaults();
        lightsaber.configFactoryDefault();

        //current limit 
        climbMtrCtrlA.setSmartCurrentLimit(currentLimit);
        climbMtrCtrlB.setSmartCurrentLimit(currentLimit);
        //set current limit for lightsaber

        //Configure MC's to brake mode
        climbMtrCtrlA.setIdleMode(IdleMode.kBrake);
        climbMtrCtrlB.setIdleMode(IdleMode.kBrake);
        lightsaber.setNeutralMode(NeutralMode.Brake);

        climbMtrCtrlB.follow(climbMtrCtrlA);
        //ClimberSpeedController = new SpeedControllerGroup(ClimbMtrCtrlNeoA, ClimbMtrCtrlNeoB);
    }

    public void runClimber(double power)
    {   
        power = Math.max(0, power);
        climbMtrCtrlA.set(power);  //fix it to the motor, dont run backward (only positive)
    }

    public void extendLightsaber(double power)
    {
        lightsaber.set(ControlMode.PercentOutput, power);
    }    
}