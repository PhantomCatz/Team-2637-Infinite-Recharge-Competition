package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class CatzClimber
{
    public WPI_TalonSRX climbMtrCtrlA; 
    public WPI_VictorSPX climbMtrCtrlB;

    private WPI_VictorSPX lightsaber;

    private int CLIMB_MC_A_CAN_ID = 30; 
    private int CLIMB_MC_B_CAN_ID = 31;

    public final int CLIMBER_MC_A_PDP_PORT = 2;
    public final int CLIMBER_MC_B_PDP_PORT = 3;

    private int LIGHTSABER_MC_CAN_ID = 50;

    public final static int LIGHTSABER_MC_PDP_PORT = 9;

    private SupplyCurrentLimitConfiguration climberSupplyCurrentLimitConfig;

    private final boolean ENABLED_CURRENT_LIMIT = true; 
    private final int CURRENT_LIMIT_AMPS = 60;
    private final int CURRENT_LIMIT_TRIGGER_AMPS = 80;
    private final int CURRENT_LIMIT_TIMEOUT_SECONDS = 5;

    private final double WINCH_SPEED = 0.5;

    public CatzClimber()
    {
        climbMtrCtrlA = new WPI_TalonSRX(CLIMB_MC_A_CAN_ID);
        climbMtrCtrlB = new WPI_VictorSPX(CLIMB_MC_B_CAN_ID);

        lightsaber = new WPI_VictorSPX(LIGHTSABER_MC_CAN_ID);

        //Reset configuration
        climbMtrCtrlA.configFactoryDefault();
        climbMtrCtrlB.configFactoryDefault();

        lightsaber.configFactoryDefault();

        //current limiting, (VictorSPX does not have current limiting capability)
        climberSupplyCurrentLimitConfig = new SupplyCurrentLimitConfiguration(ENABLED_CURRENT_LIMIT, CURRENT_LIMIT_AMPS, CURRENT_LIMIT_TRIGGER_AMPS, CURRENT_LIMIT_TIMEOUT_SECONDS);
 
        climbMtrCtrlA.configSupplyCurrentLimit(climberSupplyCurrentLimitConfig);    

        //set climb motor controller B to follow A
        climbMtrCtrlB.follow(climbMtrCtrlA);

        //Configure MC's to brake mode
        climbMtrCtrlA.setNeutralMode(NeutralMode.Brake);
        climbMtrCtrlB.setNeutralMode(NeutralMode.Brake);

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