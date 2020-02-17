package frc.Mechanisms;

import javax.naming.LimitExceededException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CatzIntake {
    //private WPI_VictorSPX intakeFigure8MtrCtrl;
    private WPI_TalonSRX intakeFigure8MtrCtrl;
    private WPI_TalonSRX intakeRollerMtrCtrl;

    private CANSparkMax intakeDeployMtrCtrl;

    private DigitalInput intakeForwardLimit;
    private DigitalInput intakeBackLimit;

    private final int INTAKE_FIGURE_8_MC_CAN_ID = 10;
    private final int INTAKE_ROLLER_MC_CAN_ID = 11;

    private final int INTAKE_DEPLOY_MC_CAN_ID = 12;

    private final int INTAKE_FORWARD_LIMIT_MC_CAN_ID = 13;
    private final int INTAKE_BACK_LIMIT_MC_CAN_ID = 14;

    public CatzIntake()
    {
        //intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeFigure8MtrCtrl = new WPI_TalonSRX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeRollerMtrCtrl = new WPI_TalonSRX (INTAKE_ROLLER_MC_CAN_ID);

        intakeDeployMtrCtrl = new CANSparkMax(INTAKE_DEPLOY_MC_CAN_ID, MotorType.kBrushless);

		intakeForwardLimit = new DigitalInput(INTAKE_FORWARD_LIMIT_MC_CAN_ID);
        intakeBackLimit    = new DigitalInput(INTAKE_BACK_LIMIT_MC_CAN_ID);
        
        //Reset configuration
        intakeFigure8MtrCtrl.configFactoryDefault();
        intakeRollerMtrCtrl.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //set the follow mode
        //intakeFigure8MtrCtrl.follow(intakeRollerMtrCtrl);

        //Set roller MC to coast mode
        intakeFigure8MtrCtrl.setNeutralMode(NeutralMode.Coast);
        intakeRollerMtrCtrl.setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);
    }

    public void rollIntake()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, -0.7);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, 0.7);
    }

    public void stopRolling()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, 0.0);
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

    public void stowIntake()
    {
        intakeDeployMtrCtrl.set(-0.5); // can change this value after testing
    }
}