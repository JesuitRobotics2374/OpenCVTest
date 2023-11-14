//Assumes NetworkTable initialized as variable before as ntInst(replace var names if already initialized other things). If false positives, add logic for validating aspect ratios.
// notes for limelight calibration:
// Low Exposure, Edit BlackLevelOffset for light pollution, Use Eyedropper for Thresholding, Highest Sort, Tri mode for target capturing (experiment w smart, need to change code), Area > 0.005 (Experiment, May not need at all), Smart Speckle on, Fullness 100, W/H Ratio > 0.75, dont touch anything unless necessary when experimenting)

import static Constants.AllianceIsRed; // If alliance is red, long ring should be on top

// periodic; constantly check if tv is 0, if it is 1 call function below: 
  

public static boolean ValidateLimelightBucketContours(){

  NetworkTable LimelightTable = ntInst.getTable("limelight");
  NetworkTableEntry tx = LimelightTable.getEntry("tx");
  NetworkTableEntry ty = LimelightTable.getEntry("ty");
  NetworkTableEntry ta = LimelightTable.getEntry("ta");

  double[] xArray = tx.getDoubleArray(new double[]{});
  double[] yArray = ty.getDoubleArray(new double[]{});
  double[] areaArray = ta.getDoubleArray(new double[]{});

  
  // Simple logic, sort by area and check orientation, rest should be done by limelight
  
  int longRectangleIndex = 0;
  int maxArea = areaArray[0];


  for (int i = 1; i < 3; i++) {
    if (areaArray[i] > maxArea) {
        longRectangleIndex = i;
        maxArea = areaArray[i];
    }
  }


  if (AllianceIsRed && yArray[longRectangleIndex] < yArray[(longRectangleIndex + 1) % 3] && yArray[longRectangleIndex] < yArray[(longRectangleIndex + 2) % 3]) {
      return true;
  } else if (!AllianceIsRed && yArray[longRectangleIndex] > yArray[(longRectangleIndex + 1) % 3] && yArray[longRectangleIndex] > yArray[(longRectangleIndex + 2) % 3]) {
      return true;
  }

  return false;
}
