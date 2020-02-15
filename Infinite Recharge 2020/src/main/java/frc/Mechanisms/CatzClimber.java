package frc.Mechanisms;

import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class CatzClimber
{
    private static CANSparkMax ClimbMtrCtrlNeoA; //rename
    private static CANSparkMax ClimbMtrCtrlNeoB;

    private static WPI_VictorSPX Lightsaber;

    private int CLIMB_NEO_A_CAN_ID = 30;
    private int CLIMB_NEO_B_CAN_ID = 31;
    private int LIGHTSABER_CAN_ID = 50;

    private static SpeedControllerGroup ClimberSpeedController;
    

    public CatzClimber()
    {
        ClimbMtrCtrlNeoA = new CANSparkMax(CLIMB_NEO_A_CAN_ID, MotorType.kBrushless);
        ClimbMtrCtrlNeoB = new CANSparkMax(CLIMB_NEO_B_CAN_ID, MotorType.kBrushless);

        //rest config

        //do follow mode
        //current limit 

        //ClimbMtrCtrlNeoA.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM)

        Lightsaber = new WPI_VictorSPX(LIGHTSABER_CAN_ID);

        ClimbMtrCtrlNeoA.setIdleMode(IdleMode.kBrake);
        ClimbMtrCtrlNeoB.setIdleMode(IdleMode.kBrake);

        Lightsaber.setNeutralMode(NeutralMode.Brake);

        //ClimberSpeedController = new SpeedControllerGroup(ClimbMtrCtrlNeoA, ClimbMtrCtrlNeoB);
    }

    public void runClimber(double power)
    {
        ClimberSpeedController.set(power);  //fix it to the motor, dont run backward (only positive)
    }

    public void extendLightsaber(double power)
    {
        Lightsaber.set(ControlMode.PercentOutput, power);
    }    
}