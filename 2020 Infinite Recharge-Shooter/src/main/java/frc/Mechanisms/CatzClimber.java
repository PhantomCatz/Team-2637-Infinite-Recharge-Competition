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

    private int CLIMB_MC_A_CAN_ID = 30;
    private int CLIMB_MC_B_CAN_ID = 31;

    public final int CLIMBER_MC_A_PDP_PORT = 2;
    public final int CLIMBER_MC_B_PDP_PORT = 3;

    private int LIGHTSABER_MC_CAN_ID = 50;

    public final static int LIGHTSABER_MC_PDP_PORT = 7;

    final private int CLIMBER_CURRENT_LIMIT = 80;
    
    public CatzClimber()

    {
        climbMtrCtrlA = new CANSparkMax(CLIMB_MC_A_CAN_ID, MotorType.kBrushless);
        climbMtrCtrlB = new CANSparkMax(CLIMB_MC_B_CAN_ID, MotorType.kBrushless);

        lightsaber = new WPI_VictorSPX(LIGHTSABER_MC_CAN_ID);

        //Reset configuration
        climbMtrCtrlA.restoreFactoryDefaults();
        climbMtrCtrlB.restoreFactoryDefaults();

        lightsaber.configFactoryDefault();

        //current limit 
        climbMtrCtrlA.setSmartCurrentLimit(CLIMBER_CURRENT_LIMIT);
        climbMtrCtrlB.setSmartCurrentLimit(CLIMBER_CURRENT_LIMIT);
        //set current limit for lightsaber

        //Configure MC's to brake mode
        climbMtrCtrlA.setIdleMode(IdleMode.kBrake);
        climbMtrCtrlB.setIdleMode(IdleMode.kBrake);

        lightsaber.setNeutralMode(NeutralMode.Brake);

        climbMtrCtrlB.follow(climbMtrCtrlA);
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