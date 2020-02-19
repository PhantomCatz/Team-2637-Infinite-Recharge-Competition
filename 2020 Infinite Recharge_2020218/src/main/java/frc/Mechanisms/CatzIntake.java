package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class CatzIntake
{
    private WPI_VictorSPX intakeRollerMtrCtrl;
    private WPI_TalonSRX  intakeDeployMtrCtrl; 

    public final int INTAKE_ROLLER_MC_CAN_ID = 10;
    public final int INTAKE_DEPLOY_MC_CAN_ID = 11;

    public final int INTAKE_ROLLER_MC_PDP_PORT   = 4;
    public final int INTAKE_DEPLOY_MC_PDP_PORT   = 11;

    public CatzIntake()
    {
        intakeRollerMtrCtrl = new WPI_VictorSPX(INTAKE_ROLLER_MC_CAN_ID);
        intakeDeployMtrCtrl = new WPI_TalonSRX (INTAKE_DEPLOY_MC_CAN_ID);

        //Reset configuration
        intakeRollerMtrCtrl.configFactoryDefault();
        intakeDeployMtrCtrl.configFactoryDefault();

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setNeutralMode(NeutralMode.Brake);

        //Set roller MC to coast mode
        intakeRollerMtrCtrl.setNeutralMode(NeutralMode.Coast);
    }

    public void rollIntake(double power)
    {
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, power); // can change this value after testing
    }

    public void deployIntake(double power)
    {
        intakeDeployMtrCtrl.set(ControlMode.PercentOutput, power); // can change this value after testing
    }
}