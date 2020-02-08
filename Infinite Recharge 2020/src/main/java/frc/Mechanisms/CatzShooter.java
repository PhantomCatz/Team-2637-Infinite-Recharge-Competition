package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class CatzShooter
{
    private WPI_TalonSRX  shtrMtrCtrlA;
    private WPI_VictorSPX shtrMtrCtrlB;

    private final int SHTR_MTR_CTRL_ID_A = 40; //CAN ID
    private final int SHTR_MTR_CTRL_ID_B = 41;

    public CatzShooter()
    {
        shtrMtrCtrlA = new WPI_TalonSRX(SHTR_MTR_CTRL_ID_A);
        shtrMtrCtrlB = new WPI_VictorSPX(SHTR_MTR_CTRL_ID_B);

        shtrMtrCtrlB.follow(shtrMtrCtrlA);
        // motor should be in coast mode
    }

    public void shooter(double power)
    {
        shtrMtrCtrlA.set(power);
    }

}