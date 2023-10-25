//Assumes NetworkTable initialized as variable before as ntInst(replace var names if already initialized other things, if new project need to initialize Limelgiht with:
//

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


  if (AllianceIsRed && y[longRectangleIndex] < y[(longRectangleIndex + 1) % 3] && y[longRectangleIndex] < y[(longRectangleIndex + 2) % 3]) {
      return true;
  } else if (!AllianceIsRed && y[longRectangleIndex] > y[(longRectangleIndex + 1) % 3] && y[longRectangleIndex] > y[(longRectangleIndex + 2) % 3]) {
      return true;
  }

  return false;
}
