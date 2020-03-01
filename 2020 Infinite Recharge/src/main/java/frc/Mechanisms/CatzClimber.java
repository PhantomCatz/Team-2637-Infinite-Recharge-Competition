package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CatzClimber
{
    public CANSparkMax climbMtrCtrlA; 
    public CANSparkMax climbMtrCtrlB;

    private WPI_VictorSPX lightsaber;

    private int CLIMB_MC_A_CAN_ID = 30; 
    private int CLIMB_MC_B_CAN_ID = 31;

    public final int CLIMBER_MC_A_PDP_PORT = 2;
    public final int CLIMBER_MC_B_PDP_PORT = 3;

    private int LIGHTSABER_MC_CAN_ID = 50;

    public final static int LIGHTSABER_MC_PDP_PORT = 9;

    private final double WINCH_SPEED = 0.5;

    private final int CLIMBER_MC_CURRENT_LIMIT = 80;

    public CatzClimber()
    {
        climbMtrCtrlA = new CANSparkMax(CLIMB_MC_A_CAN_ID, MotorType.kBrushed);
        climbMtrCtrlB = new CANSparkMax(CLIMB_MC_B_CAN_ID, MotorType.kBrushed);

        lightsaber = new WPI_VictorSPX(LIGHTSABER_MC_CAN_ID);

        //Reset configuration
        climbMtrCtrlA.restoreFactoryDefaults();
        climbMtrCtrlB.restoreFactoryDefaults();

        lightsaber.configFactoryDefault();

        //set current Limiting
        climbMtrCtrlA.setSmartCurrentLimit(CLIMBER_MC_CURRENT_LIMIT);
        climbMtrCtrlB.setSmartCurrentLimit(CLIMBER_MC_CURRENT_LIMIT);

        //set climb motor controller B to follow A
        climbMtrCtrlB.follow(climbMtrCtrlA);

        //Configure MC's to brake mode
        climbMtrCtrlA.setIdleMode(IdleMode.kBrake);
        climbMtrCtrlB.setIdleMode(IdleMode.kBrake);

        lightsaber.setNeutralMode(NeutralMode.Brake);
    }

    public void runWinch()
    {   
        climbMtrCtrlA.set(WINCH_SPEED);  
    }

    public void extendLightsaber()
    {
        lightsaber.set(ControlMode.PercentOutput, 0.5);
    }   
    
    public void retractLightsaber()
    {
        lightsaber.set(ControlMode.PercentOutput, 0.5);

    }
}