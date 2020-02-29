package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;


public class CatzIntake {

    // this is the motor controller for the competition robot

    //private WPI_VictorSPX intakeFigure8MtrCtrl;

    private WPI_TalonSRX intakeFigure8MtrCtrl;
    public WPI_TalonSRX  intakeRollerMtrCtrl; //changed to public because not working on drivetrain

    private CANSparkMax intakeDeployMtrCtrl;

    private CANDigitalInput intakeDeployedLimitSwitch;
    private CANDigitalInput intakeStowedLimitSwitch;

    public static LimitSwitchPolarity intakeDeployMtrCtrlPolarity = LimitSwitchPolarity.kNormallyClosed;

    private final int INTAKE_FIGURE_8_MC_CAN_ID = 10;
    private final int INTAKE_ROLLER_MC_CAN_ID   = 11;
    private final int INTAKE_DEPLOY_MC_CAN_ID   = 12;

    // initial state of intake when round starts

    public boolean deployed = false; //TBD should only be one boolean
    public boolean stowed   = true;

    public CatzIntake()
    {
        // this is the motor controller for the competition robot
        //intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID); 

        intakeFigure8MtrCtrl = new WPI_TalonSRX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeRollerMtrCtrl  = new WPI_TalonSRX(INTAKE_ROLLER_MC_CAN_ID);
        intakeDeployMtrCtrl  = new CANSparkMax (INTAKE_DEPLOY_MC_CAN_ID, MotorType.kBrushless);

        intakeDeployedLimitSwitch = intakeDeployMtrCtrl.getForwardLimitSwitch(intakeDeployMtrCtrlPolarity);
        intakeStowedLimitSwitch   = intakeDeployMtrCtrl.getReverseLimitSwitch(intakeDeployMtrCtrlPolarity);

        //Reset configuration
        intakeFigure8MtrCtrl.configFactoryDefault();
        intakeRollerMtrCtrl.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //Set roller MC to coast mode
        intakeFigure8MtrCtrl.setNeutralMode(NeutralMode.Coast);
        intakeRollerMtrCtrl.setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);

    }



    // ---------------------------------------------ROLLER---------------------------------------------

    public void intakeRollerIn()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, -0.7);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, 0.7);
    }

    public void intakeRollerOut()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, 0.7);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, -0.7);
    }

    public void intakeRollerOff()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, 0.0);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, 0.0);
    }

    // ---------------------------------------------DEPLOY/STOW---------------------------------------------
    public void deployIntake()
    {
        intakeDeployMtrCtrl.set(0.23);
    }

    public void stowIntake()
    {
        intakeDeployMtrCtrl.set(-0.23);
    }

    public void stopDeploying()
    {
        intakeDeployMtrCtrl.set(0);
    }

    // ---------------------------------------------Intake Limit Switches---------------------------------------------   

    public boolean getDeployedLimitSwitchState()
    {
        return intakeDeployedLimitSwitch.get();
    }

    public boolean getStowedLimitSwitchState()
    {
        return intakeStowedLimitSwitch.get();
    }
}