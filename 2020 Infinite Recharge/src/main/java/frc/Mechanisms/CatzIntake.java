
package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CatzIntake 
{
    public WPI_VictorSPX intakeFigure8MtrCtrl;
    public WPI_TalonSRX  intakeRollerMtrCtrl;

    private CANSparkMax intakeDeployMtrCtrl;

    private CANDigitalInput intakeDeployedLimitSwitch;
    private CANDigitalInput intakeStowedLimitSwitch;

    private static LimitSwitchPolarity intakeDeployMtrCtrlPolarity;

    private final int INTAKE_FIGURE_8_MC_CAN_ID = 10;
    private final int INTAKE_ROLLER_MC_CAN_ID = 11;

    private final int INTAKE_DEPLOY_MC_CAN_ID = 12;

    private final boolean DEPLOYED = false;
    private final boolean STOWED   = true;

    private boolean intakeState = STOWED;
    
    private SupplyCurrentLimitConfiguration intakeCurrentLimit;

    private boolean enableCurrentLimit = true; 
    private int currentLimitAmps = 60;
    private int currentLimitTriggerAmps = 80;
    private int currentLimitTimeoutSeconds = 5;

    public CatzIntake()
    {
        intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeRollerMtrCtrl  = new WPI_TalonSRX (INTAKE_ROLLER_MC_CAN_ID);

        intakeDeployMtrCtrl = new CANSparkMax(INTAKE_DEPLOY_MC_CAN_ID, MotorType.kBrushless);

        //Reset configuration
        intakeFigure8MtrCtrl.configFactoryDefault();
        intakeRollerMtrCtrl.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //Set roller MC to coast mode
        intakeFigure8MtrCtrl.setNeutralMode(NeutralMode.Coast);
        intakeRollerMtrCtrl.setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);

        //Set current limit configuration (VictorSPX does not have current limiting capabilites)
        intakeRollerMtrCtrl.configSupplyCurrentLimit(intakeCurrentLimit);
        
        intakeDeployMtrCtrl.setSmartCurrentLimit(currentLimitAmps);

        //Limit Switches Configuration
        intakeDeployMtrCtrlPolarity = LimitSwitchPolarity.kNormallyClosed;

        intakeDeployedLimitSwitch = intakeDeployMtrCtrl.getForwardLimitSwitch(intakeDeployMtrCtrlPolarity);
        intakeStowedLimitSwitch   = intakeDeployMtrCtrl.getReverseLimitSwitch(intakeDeployMtrCtrlPolarity);
    }

    // ---------------------------------------------ROLLER---------------------------------------------
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

    // ---------------------------------------------DEPLOY/STOW---------------------------------------------
    public void deployIntake()
    {
        intakeDeployMtrCtrl.set(0.23);
        intakeState = DEPLOYED;
    }
    public void stowIntake()
    {
        intakeDeployMtrCtrl.set(-0.23);
        intakeState = STOWED;
    }
    public void stopDeploying()
    {
        intakeDeployMtrCtrl.set(0);
    }
    public boolean getIntakeDeployState()
    {
        return intakeState;
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