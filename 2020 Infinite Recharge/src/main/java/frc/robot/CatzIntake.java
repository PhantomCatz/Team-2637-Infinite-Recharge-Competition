package frc.robot;

import edu.wpi.first.wpilibj.Talon;

public class CatzIntake
{
    private static WPI_TalonSRX intakeMtrCtrl; 
    private final int INTAKE_MC_CAN_ID = 0;

    public CatzIntake()
    {
        intakeWristMtrCtrl = new WPI_TalonSRX(INTAKE_MC_CAN_ID);
    }

    public void rotateIntake(double power)
    {
        intakeWristMtrCtrl.set(power);
    }
}