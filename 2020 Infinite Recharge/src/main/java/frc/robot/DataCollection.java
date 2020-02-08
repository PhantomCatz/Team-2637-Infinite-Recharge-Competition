package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.Mechanisms.CatzDriveTrain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import frc.Mechanisms.*;

public class DataCollection 
{

    public static int robotDataType;
    private Thread dataThread;

    public static ArrayList<CatzLog> robotDataList;
    public static boolean threadFlag = false;
    StringBuilder sb = new StringBuilder();

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");

    public void setDataType(int dataType)
    {
        robotDataType = dataType;
        /* 
        Key 1 = DriveTrain voltage, DriveTrain current, DriveTrain Temperature
        key 2 = Position FOC
        key 3 = Position ENC */
    
    }

    public void dataCollectionInit(ArrayList<CatzLog> list)
    {   
        robotDataList = list;
        dataThread = new Thread(() ->
        {
            while(!Thread.interrupted())
            {   
                if(threadFlag == true)
                {
                    switch(robotDataType)
                    {
                        case 1 :
                        collectData(1);
                        break;
                        
                        case 2 :
                        collectData(2);
                        break;

                        case 3 :
                        collectData(3);
                        break;

                        case 4 : 
                        collectData(4);
                        break;

                        case 5 :
                        collectData(1);
                        collectData(2);
                        collectData(3);
                        collectData(4);
                        break;

                    }


                } 
                else if (threadFlag == false) 
                {
                 
                } 

                Timer.delay(0.1);

            }

        });

        dataThread.start();

    }

    public void startDataCollection() 
    {
        threadFlag = true;
    }

    public void collectData(int dataType)
    {
        CatzLog data;
        double data1 = 0.0;
        double data2 = 0.0;
        double data3 = 0.0;
        double data4 = 0.0;
        double data5 = 0.0;
        double data6 = 0.0;
        double data7 = 0.0;
        double data8 = 0.0;
        double data9 = 0.0;
        double data10 = 0.0;
        double data11 = 0.0;
        double data12 = 0.0;

        boolean validValue = true;

        switch (dataType) 
        {
            case 1 :
                data1 = Robot.pdp.getVoltage();

                data2 = Robot.pdp.getCurrent(1);
                data3 = Robot.pdp.getCurrent(2);
                data4 = Robot.pdp.getCurrent(4);
                data5 = Robot.pdp.getCurrent(3);
/*
                data6 = Robot.driveTrain.getMotorTemperature(1);
                data7 = Robot.driveTrain.getMotorTemperature(2);
                data8 = Robot.driveTrain.getMotorTemperature(4);
                data9 = Robot.driveTrain.getMotorTemperature(3);

                data10 = CatzDriveTrain.getFOCLT();
                data11 = CatzDriveTrain.getFOCRT();

                data12 = CatzDriveTrain.getSRXMagEncPosition(); */
                break;

            case 2 :
          /*      data1 = CatzDriveTrain.getFOCLT();
                data2 = CatzDriveTrain.getFOCRT();  */
                break; 

            case 3 :

                data1 = CatzDriveTrain.getRTEncVelocity();
                data2 = CatzDriveTrain.getError();    

                data3 = CatzDriveTrain.RT_kP;
                data4 = CatzDriveTrain.RT_kI;
                data5 = CatzDriveTrain.RT_kD;
                data6 = CatzDriveTrain.RT_kF;
                data7 = CatzDriveTrain.RT_kIz;

                break;
            


            default :
                validValue = false;

        }

        if(validValue == true) 
        {
            data = new CatzLog(Robot.t.get(), data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12);
            robotDataList.add(data);
        }
    }

    public void stopDataCollection() 
    {
        threadFlag = false; 
    }

    public void writeHeader() throws IOException {
        try (
        FileWriter fw = new FileWriter("//media//sda1//RobotData.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw))
        
        {
            if(robotDataType == 1) 
            {
                sb.append("Time");
                sb.append(", ");
                sb.append("Voltage 1");
                sb.append(", ");
                sb.append("Voltage 2");
                sb.append(", ");
                sb.append("Voltage 3");
                sb.append(", ");
                sb.append("Voltage 4");      
                sb.append("\n");
            }

            {
            if(robotDataType == 2) 
            {
                    sb.append("Time");
                    sb.append(", ");
                    sb.append("Current 1");
                    sb.append(", ");
                    sb.append("Current 2");
                    sb.append(", ");
                    sb.append("Current 3");
                    sb.append(", ");
                    sb.append("Current 4");     
                    sb.append("\n"); 
            }

            {
            if(robotDataType == 3) 
            {
                sb.append("Time");
                sb.append(", ");
                sb.append("Temp 1");
                sb.append(", ");
                sb.append("Temp 2");
                sb.append(", ");
                sb.append("Temp 3");
                sb.append(", ");
                sb.append("Temp 4");  
                sb.append("\n");    
            }

                {
                if(robotDataType == 4) 
                {
                    sb.append("Time");
                    sb.append(", ");
                    sb.append("Position 1");
                    sb.append(", ");
                    sb.append("Position 2");
                    sb.append(", ");
                    sb.append("Position 3");
                    sb.append(", ");
                    sb.append("Position 4"); 
                    sb.append("\n");     
                }
                } 
            }
            }   
        }

        System.out.println("end of header writing segment");
}

    public void writeData(ArrayList<CatzLog> data) 
    { 
        // create columns labeling data
        /*sb.append("Voltage" + "\t\t");
        sb.append("Current" + "\t\t");
        sb.append("Temp(F)" + "\t\t");
        sb.append("Position");*/
        //sb.append("\n*********************************************************\n");

        // length of a singular data set
        int dataSetLength = 12;
        
        // loop through arraylist and adds it to the StringBuilder
        for (int i = 0; i < data.size(); i++)
        {
            //sb.append(data.get(i) + "\t\t");
          /*  
            if (((i + 1) % dataSetLength) == 0)
            {
                sb.append(data.get(i) + ",");
            } 
            else */
            {
                sb.append(data.get(i) + "\n");
            }
        }
    }

    // print out data after fully updated
    public void exportData() throws IOException
    {       
        try (
        FileWriter fw = new FileWriter("//media//sda1//RobotData.csv", true);
        //FileWriter fw = new FileWriter("ftp:////roboRIO-2637-FRC.local//ESD-USB//RobotData.txt", true);
        //FileWriter fw = new FileWriter("C:\\Users\\justi\\Documents\\FRC.txt", true);
        //FileWriter fw = new FileWriter("/media/sda1/RobotData.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw))          
        {
            pw.print(sb.toString());
            pw.close();
        }
    }
}