package frc.Autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CatzAutonomous
{

    public static boolean checkBoxL;
    public static boolean checkBoxM;
    public static boolean checkBoxR;

    public void choosePath()
    {


        checkBoxL = SmartDashboard.getBoolean("Position Left", false);
        checkBoxM = SmartDashboard.getBoolean("Position Middle", false);
        checkBoxR = SmartDashboard.getBoolean("Position Right", false);

        //red and blue sides symmetrical, no check box for color required

        if(checkBoxL == true)
        {

        }
        else if(checkBoxM == true)
        {

        }
        else if(checkBoxR == true)
        {

        }
        else
        {
            System.out.println("no path was chosen - performing default");

            //drive past starting line and stop

        }


    }


}