package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class CatzIntake
{
    private static WPI_TalonSRX LTIntakeMtrCtrl; // victors, not talons
    private static WPI_TalonSRX RTIntakeMtrCtrl; // one is a deployer, one is a roller

    private final int LT_INTAKE_MC_CAN_ID = 10;
    private final int RT_INTAKE_MC_CAN_ID = 11; 

    public CatzIntake()
    {
        LTIntakeMtrCtrl = new WPI_TalonSRX(LT_INTAKE_MC_CAN_ID);
        RTIntakeMtrCtrl = new WPI_TalonSRX(RT_INTAKE_MC_CAN_ID);
    }

    public void activateRollers(double power) // don't pass in a parameter
    {
        LTIntakeMtrCtrl.set(power);
        RTIntakeMtrCtrl.set(power);
    }
}