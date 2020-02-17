package frc.DataLogger;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;

import frc.robot.Robot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataCollection 
{	
    static Date date = Calendar.getInstance().getTime();
    static SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_kkmmss");
    static String dateFormatted = sdf.format(date);

    public static boolean fileNotAppended = false;

    public final static String logDataFilePath = "//media//sda1//RobotData";
    public final static String logDataFileType = ".csv";

    private Thread dataThread;

    public static boolean logDataValues = false;
    public static int logDataID;

    public static ArrayList<CatzLog> logData;

    StringBuilder sb = new StringBuilder();

    private final double LOG_SAMPLE_RATE = 0.1;

    public final static int LOG_ID_DRV_TRAIN = 1;
    public final static int LOG_ID_DRV_STRAIGHT_PID = 2;
    public final static int LOG_ID_DRV_DISTANCE_PID = 3;
    public final static int LOG_ID_DRV_TURN_PID = 4;
    public final static int LOG_ID_SHOOTER = 5;

    private final static String LOG_HDR_DRV_TRAIN = "time,pdp-v,dt-lf-I,dt-lb-I,dt-rf-I,dt-rb-I,dt-lf-T,dt-lb-T,dt-rf-T,dt-rb-T,dt-l-ie,dt-r-ie,dt-l-ee,dt-r-ee";
    private final static String LOG_HDR_DRV_STRAIGHT_PID = "time,dt-l-iev,dt-l-pwr,dt-l-mcI,dt-l-cle,dt-l-errD,dt-l-iAcc,"
            + "dt-r-iev,dt-r-pwr,dt-r-mcI,dt-r-cle,dt-r-errD,dt-r-iAcc";
    private final static String LOG_HDR_DRV_DISTANCE_PID = "Undefined";
    private final static String LOG_HDR_DRV_TURN_PID = "Undefined";
    private final static String LOG_HDR_SHOOTER = "Undefined";

    public String logStr;

    public void setLogDataID(final int dataID) {
        logDataID = dataID;

    }

    public void dataCollectionInit(final ArrayList<CatzLog> list) {
        logData = list;

        dataThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                if (logDataValues == true) {
                    collectData(logDataID);
                } else if (logDataValues == false) {

                }

                Timer.delay(LOG_SAMPLE_RATE);

            }

        });

        dataThread.start();
    }

    public static void startDataCollection() {
        logDataValues = true;
    }

    public static void stopDataCollection() {
        logDataValues = false;
    }

    public void collectData(final int dataID) {
        CatzLog data;
        double data1 = -999.0;
        double data2 = -999.0;
        double data3 = -999.0;
        double data4 = -999.0;
        double data5 = -999.0;
        double data6 = -999.0;
        double data7 = -999.0;
        double data8 = -999.0;
        double data9 = -999.0;
        double data10 = -999.0;
        double data11 = -999.0;
        double data12 = -999.0;
        double data13 = -999.0;

        boolean validLogID = true;

        switch (dataID) {
        case LOG_ID_DRV_TRAIN:
            data1 = Robot.pdp.getVoltage();

            data2 = Robot.pdp.getCurrent(Robot.DriveTrain.DRV_TRN_LT_FRNT_MC_PDP_PORT);
            data3 = Robot.pdp.getCurrent(Robot.DriveTrain.DRV_TRN_LT_BACK_MC_PDP_PORT);
            data4 = Robot.pdp.getCurrent(Robot.DriveTrain.DRV_TRN_RT_FRNT_MC_PDP_PORT);
            data5 = Robot.pdp.getCurrent(Robot.DriveTrain.DRV_TRN_RT_BACK_MC_PDP_PORT);

            data6 = Robot.DriveTrain.getMotorTemperature(Robot.DriveTrain.DRVTRAIN_LT_FRNT_MC_CAN_ID);
            data7 = Robot.DriveTrain.getMotorTemperature(Robot.DriveTrain.DRVTRAIN_LT_BACK_MC_CAN_ID);
            data8 = Robot.DriveTrain.getMotorTemperature(Robot.DriveTrain.DRVTRAIN_RT_FRNT_MC_CAN_ID);
            data9 = Robot.DriveTrain.getMotorTemperature(Robot.DriveTrain.DRVTRAIN_RT_BACK_MC_CAN_ID);

            data10 = Robot.DriveTrain.getIntegratedEncPosition("LT");
            data11 = Robot.DriveTrain.getIntegratedEncPosition("RT");

            data12 = Robot.DriveTrain.getSrxMagPosition("LT");
            data13 = Robot.DriveTrain.getSrxMagPosition("RT");

            break;

        case LOG_ID_DRV_STRAIGHT_PID:

            data1 = Robot.DriveTrain.getIntegratedEncVelocity("LT");
            data2 = Robot.DriveTrain.drvTrainMtrCtrlLTFrnt.getMotorOutputPercent();
            data3 = Robot.DriveTrain.drvTrainMtrCtrlLTFrnt.getStatorCurrent();
            data4 = (double) Robot.DriveTrain.drvTrainMtrCtrlLTFrnt.getClosedLoopError(0);
            data5 = Robot.DriveTrain.drvTrainMtrCtrlLTFrnt.getErrorDerivative();
            data6 = Robot.DriveTrain.drvTrainMtrCtrlLTFrnt.getIntegralAccumulator(0);

            data7 = Robot.DriveTrain.getIntegratedEncVelocity("RT");
            data8 = Robot.DriveTrain.drvTrainMtrCtrlRTFrnt.getMotorOutputPercent();
            data9 = Robot.DriveTrain.drvTrainMtrCtrlRTFrnt.getStatorCurrent();
            data10 = (double) Robot.DriveTrain.drvTrainMtrCtrlRTFrnt.getClosedLoopError(0);
            data11 = Robot.DriveTrain.drvTrainMtrCtrlRTFrnt.getErrorDerivative();
            data12 = Robot.DriveTrain.drvTrainMtrCtrlRTFrnt.getIntegralAccumulator(0);

            break;

        case 3:

            data9 = Robot.DriveTrain.getSrxMagPosition("LT");
            data10 = Robot.DriveTrain.getSrxMagPosition("RT");

            break;

        case LOG_ID_SHOOTER:
            break;

        default:
            validLogID = false;

        }

        if (validLogID == true) {
            data = new CatzLog(Robot.dataCollectionTimer.get(), data1, data2, data3, data4, data5, data6, data7, data8,
                    data9, data10, data11, data12, data13);
            logData.add(data);
        }
    }

    public static void writeHeader(PrintWriter pw) {
        switch (logDataID) {
        case LOG_ID_DRV_TRAIN:
            pw.printf(LOG_HDR_DRV_TRAIN);
            break;
        case LOG_ID_DRV_STRAIGHT_PID:
            pw.printf(LOG_HDR_DRV_STRAIGHT_PID);
            break;
        case LOG_ID_DRV_DISTANCE_PID:
            pw.printf(LOG_HDR_DRV_DISTANCE_PID);
            break;
        case LOG_ID_DRV_TURN_PID:
            pw.printf(LOG_HDR_DRV_TURN_PID);
            break;
        case LOG_ID_SHOOTER:
            pw.printf(LOG_HDR_SHOOTER);
            break;
        default:
            pw.printf("Invalid Log Data ID");

        }
    }

    public static String createFilePath() {
        String logDataFullFilePath = logDataFilePath + dateFormatted + logDataFileType;
        return logDataFullFilePath;
    }

    // print out data after fully updated
    public static void exportData(ArrayList<CatzLog> data) throws IOException {
        try (FileWriter fw = new FileWriter(createFilePath(), fileNotAppended);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw))

        {
            writeHeader(pw);

            // loop through arraylist and adds it to the StringBuilder
            int dataSize = data.size();
            for (int i = 0; i < dataSize; i++)
            {
                pw.print(data.get(i).toString() + "\n");
                pw.flush();
            }

            pw.close();
        }
    }
}