package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import org.ejml.dense.row.linsol.qr.BaseLinearSolverQrp_DDRM;

import edu.wpi.first.networktables.NetworkTable;

public class Limelight {

    private static NetworkTable tableInst = NetworkTableInstance.getDefault().getTable("limelight");
    private static NetworkTableEntry tv = tableInst.getEntry("tv");
    private static NetworkTableEntry ta = tableInst.getEntry("ta");
    private static NetworkTableEntry ty = tableInst.getEntry("ty");
    private static NetworkTableEntry tx = tableInst.getEntry("tx");

    public Limelight(){};

    public boolean hasTarget(){
        return tv.getDouble(0) == 1.0;
    }
    
    public double[] getTxArray(){
        return tx.getDoubleArray(new double[3]);
    }

    public double getTx(){
        return tx.getDouble(3);
    }

    public double getTy(){
        return ty.getDouble(3);
    }

    public double getTa(){
        return ta.getDouble(3);
    }

    public double getTv(){
        return tv.getDouble(3);
    }

    public double[] getTyArray(){
        return ty.getDoubleArray(new double[3]);
    }    

    public double[] getTaArray(){
        return ta.getDoubleArray(new double[3]);
    }

    


}