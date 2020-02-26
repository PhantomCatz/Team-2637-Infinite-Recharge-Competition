package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

<<<<<<< Updated upstream
public class CatzIntake
{
    private WPI_VictorSPX intakeRollerMtrCtrlA;
    private WPI_TalonSRX  intakeRollerMtrCtrlB;
=======
public class CatzIntake 
{
    private WPI_VictorSPX intakeFigure8MtrCtrl;
    private WPI_TalonSRX intakeRollerMtrCtrl;
>>>>>>> Stashed changes

    private CANSparkMax  intakeDeployMtrCtrl; 

    public final int INTAKE_ROLLER_A_MC_CAN_ID = 10;
    public final int INTAKE_ROLLER_B_MC_CAN_ID = 11;

    public final int INTAKE_DEPLOY_MC_CAN_ID = 12;

    public final int INTAKE_ROLLER_A_MC_PDP_PORT   = 4;
    public final int INTAKE_ROLLER_B_MC_PDP_PORT   = 9;   

    public final int INTAKE_DEPLOY_MC_PDP_PORT   = 11;

    public CatzIntake()
    {
<<<<<<< Updated upstream
        intakeRollerMtrCtrlA = new WPI_VictorSPX(INTAKE_ROLLER_A_MC_CAN_ID);
        intakeRollerMtrCtrlB = new WPI_TalonSRX(INTAKE_ROLLER_B_MC_CAN_ID);
=======
        //intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeFigure8MtrCtrl = new WPI_VictorSPX(INTAKE_FIGURE_8_MC_CAN_ID);
        intakeRollerMtrCtrl = new WPI_TalonSRX (INTAKE_ROLLER_MC_CAN_ID);
>>>>>>> Stashed changes

        intakeDeployMtrCtrl = new CANSparkMax(INTAKE_ROLLER_B_MC_CAN_ID, MotorType.kBrushless);

        //Reset configuration
        intakeRollerMtrCtrlA.configFactoryDefault();
        intakeRollerMtrCtrlB.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //set the follow mode 
        intakeRollerMtrCtrlA.follow(intakeRollerMtrCtrlB);

        //Set roller MC to coast mode
        intakeRollerMtrCtrlA.setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);
    }

    public void rollIntake()
    {
<<<<<<< Updated upstream
        intakeRollerMtrCtrlB.set(ControlMode.PercentOutput, -0.72); // can change this value after testing
=======
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, 0.5);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, -0.5);
    }

    public void stopRolling()
    {
        intakeFigure8MtrCtrl.set(ControlMode.PercentOutput, 0.0);
        intakeRollerMtrCtrl.set(ControlMode.PercentOutput, 0.0);
    }

    public void stopDeploying()
    {
        intakeDeployMtrCtrl.set(0);
>>>>>>> Stashed changes
    }

    public void deployIntake()
    {
        intakeDeployMtrCtrl.set(0.4); // can change this value after testing
    }
}