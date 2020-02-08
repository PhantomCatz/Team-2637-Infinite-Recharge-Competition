package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class CatzIntake
{
    //private WPI_VictorSPX intakeRollerMtrCtrl;
    private WPI_TalonSRX intakeRollerMtrCtrl;
    private WPI_TalonSRX intakeDeployMtrCtrl; 

    //private final int INTAKEROLLER_MC_CAN_ID = 10;
    private final int INTAKEROLLER_MC_CAN_ID = 15;
    private final int INTAKEDEPLOY_MC_CAN_ID = 11;

    public CatzIntake()
    {
        //intakeRollerMtrCtrl = new WPI_VictorSPX(INTAKEROLLER_MC_CAN_ID);
        intakeRollerMtrCtrl = new WPI_TalonSRX(INTAKEROLLER_MC_CAN_ID);
        intakeDeployMtrCtrl = new WPI_TalonSRX (INTAKEDEPLOY_MC_CAN_ID);

        intakeDeployMtrCtrl.setNeutralMode(NeutralMode.Brake);
    }

    public void rollIntake()
    {
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, -0.72); // can change this value after testing
    }

    public void deployIntake()
    {
        //intakeDeployMtrCtrl.set(ControlMode.PercentOutput, 0.4); // can change this value after testing
    }
}