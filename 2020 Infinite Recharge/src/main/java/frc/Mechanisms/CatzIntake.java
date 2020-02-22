package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CatzIntake
{
    private WPI_VictorSPX intakeRollerMtrCtrlA;
    private WPI_TalonSRX  intakeRollerMtrCtrlB;

    private CANSparkMax  intakeDeployMtrCtrl; 

    public final int INTAKE_ROLLER_A_MC_CAN_ID = 10;
    public final int INTAKE_ROLLER_B_MC_CAN_ID = 11;

    public final int INTAKE_DEPLOY_MC_CAN_ID = 12;

    public final int INTAKE_ROLLER_A_MC_PDP_PORT   = 4;
    public final int INTAKE_ROLLER_B_MC_PDP_PORT   = 9;   

    public final int INTAKE_DEPLOY_MC_PDP_PORT   = 11;

    public CatzIntake()
    {
        intakeRollerMtrCtrlA = new WPI_VictorSPX(INTAKE_ROLLER_A_MC_CAN_ID);
        intakeRollerMtrCtrlB = new WPI_TalonSRX(INTAKE_ROLLER_B_MC_CAN_ID);

        intakeDeployMtrCtrl = new CANSparkMax(INTAKE_ROLLER_B_MC_CAN_ID, MotorType.kBrushless);

        //Reset configuration
        intakeRollerMtrCtrlA.configFactoryDefault();
        intakeRollerMtrCtrlB.configFactoryDefault();

        intakeDeployMtrCtrl.restoreFactoryDefaults();

        //set the follow mode 
        intakeRollerMtrCtrlA.follow(intakeRollerMtrCtrlB);

        //Set roller MC to coast mode
<<<<<<< HEAD
        intakeRollerMtrCtrl.setNeutralMode(NeutralMode.Coast);

=======
        intakeRollerMtrCtrlA.setNeutralMode(NeutralMode.Coast);

        //Set deploy MC to brake mode
        intakeDeployMtrCtrl.setIdleMode(IdleMode.kBrake);
>>>>>>> master
    }

    public void rollIntake()
    {
        intakeRollerMtrCtrlB.set(ControlMode.PercentOutput, -0.72); // can change this value after testing
    }

    public void deployIntake()
    {
        intakeDeployMtrCtrl.set(0.4); // can change this value after testing
    }

    public int getDrvTrainLTPosition()
    {
        return intakeDeployMtrCtrl.getSensorCollection().getQuadraturePosition(); 
    }
    public int getDrvTrainLTVelocity()
    {
        return intakeDeployMtrCtrl.getSensorCollection().getQuadratureVelocity(); 
    }
}