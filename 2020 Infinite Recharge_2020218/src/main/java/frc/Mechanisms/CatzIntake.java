package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

public class CatzIntake
{
    private WPI_VictorSPX intakeFigure8MtrCtrl;
    private WPI_TalonSRX  intakeRollerMtrCtrl;

    private CANSparkMax  intakeDeployMtrCtrl; 

    public final int INTAKE_FIGURE_8_MC_CAN_ID = 10;
    public final int INTAKE_ROLLER_MC_CAN_ID = 11;

    public final int INTAKE_DEPLOY_MC_CAN_ID = 12;

    public final int INTAKE_FIGURE_8_MC_PDP_PORT   = 5;
    public final int INTAKE_ROLLER_MC_PDP_PORT   = 9;   

    public final int INTAKE_DEPLOY_MC_PDP_PORT   = 11;

    private DigitalInput intakeForwardLimit;
    private DigitalInput intakeBackLimit; 

    private final int INTAKE_FORWARD_LIMIT_DIO_PORT = 0;
    private final int INTAKE_BACK_LIMIT_DIO_PORT = 1;

    public CatzIntake()
    {
        intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeRollerMtrCtrl = new WPI_TalonSRX(INTAKE_ROLLER_MC_CAN_ID);

        intakeDeployMtrCtrl = new CANSparkMax(INTAKE_DEPLOY_MC_CAN_ID, MotorType.kBrushless);

        //Reset configuration
        intakeFigure8MtrCtrl.configFactoryDefault();
        intakeRollerMtrCtrl.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //set the follow mode 
        intakeFigure8MtrCtrl.follow(intakeRollerMtrCtrl);
        intakeFigure8MtrCtrl.setInverted(true);

        //Set roller MC to coast mode
        intakeFigure8MtrCtrl.setNeutralMode(NeutralMode.Coast);
        intakeRollerMtrCtrl. setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);

        intakeForwardLimit = new DigitalInput(INTAKE_FORWARD_LIMIT_DIO_PORT);
        intakeBackLimit    = new DigitalInput(INTAKE_BACK_LIMIT_DIO_PORT);

    }

    public void rollIntake(double power)
    {
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, power); // can change this value after testing
        
    }

    public void stopRolling()
    {
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, 0.0);
    }

    public void stopDeploying()
    {
        intakeDeployMtrCtrl.set(0);
    }

    public void deployIntake()
    {
        intakeDeployMtrCtrl.set(0.5); // can change this value after testing
    }

    public void deployIntake(double power)
    {
        intakeDeployMtrCtrl.set(power); // can change this value after testing
    }

    public void stowIntake()
    {
        intakeDeployMtrCtrl.set(-0.5); // can change this value after testing
    }
}