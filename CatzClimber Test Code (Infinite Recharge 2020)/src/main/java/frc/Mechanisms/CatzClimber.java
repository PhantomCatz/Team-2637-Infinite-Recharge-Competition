package frc.Mechanisms;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX; 
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
public class CatzClimber
{
    private WPI_TalonSRX climbMtrCtrlA; 
    private WPI_TalonSRX climbMtrCtrlB;

    private WPI_VictorSPX lightsaber;

    private SpeedControllerGroup climbMotors;

    private int CLIMB_MC_A_CAN_ID = 1;
    private int CLIMB_MC_B_CAN_ID = 2;

    public final int CLIMBER_MC_A_PDP_PORT = 2;
    public final int CLIMBER_MC_B_PDP_PORT = 3;

    private int LIGHTSABER_MC_CAN_ID = 50;
    
    private SupplyCurrentLimitConfiguration climberCurrentLimit;

    private boolean enableCurrentLimit = true; 
    private int currentLimitAmps = 60;
    private int currentLimitTriggerAmps = 80;
    private int currentLimitTimeoutSeconds = 5;

    public final static int LIGHTSABER_MC_PDP_PORT = 5;

  
    
    public CatzClimber()

    {
        climbMtrCtrlA = new WPI_TalonSRX(CLIMB_MC_A_CAN_ID);
        climbMtrCtrlB = new WPI_TalonSRX(CLIMB_MC_B_CAN_ID);

        lightsaber = new WPI_VictorSPX(LIGHTSABER_MC_CAN_ID);

        climbMotors = new SpeedControllerGroup(climbMtrCtrlA, climbMtrCtrlB);

        //Reset configuration
        climbMtrCtrlA.configFactoryDefault();
        climbMtrCtrlB.configFactoryDefault();

        lightsaber.configFactoryDefault();

        climberCurrentLimit = new SupplyCurrentLimitConfiguration(enableCurrentLimit, currentLimitAmps, currentLimitTriggerAmps, currentLimitTimeoutSeconds);

        //current limit 
        climbMtrCtrlA.configSupplyCurrentLimit(climberCurrentLimit);
        climbMtrCtrlB.configSupplyCurrentLimit(climberCurrentLimit);
        //set current limit for lightsaber

        //Configure MC's to brake mode
        climbMtrCtrlA.setNeutralMode(NeutralMode.Brake);
        climbMtrCtrlB.setNeutralMode(NeutralMode.Brake);

        lightsaber.setNeutralMode(NeutralMode.Brake);

        climbMtrCtrlB.follow(climbMtrCtrlA);
    }

    public void runClimber(double power)
    {   
        power = Math.max(0, power);
        climbMotors.set(power);  //fix it to the motor, dont run backward (only positive)
    }

    public void extendLightsaber(double power)
    {
        lightsaber.set(ControlMode.PercentOutput, power);
    }    
}