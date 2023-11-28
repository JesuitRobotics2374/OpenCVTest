package frc.robot;

import static frc.robot.Constants.IsTeamRed;

public class Utils {

    Limelight limelight = new Limelight();

    public boolean validateTarget(){

        double[] yArray = limelight.getTyArray();
        double[] areaArray = limelight.getTaArray();
        double[] xArray = limelight.getTxArray();

        // System.out.println(areaArray.toString());

        // Simple logic, sort by area and check orientation, rest should be done by limelight
        int c = 0;
        int longRectangleIndex = 0;
        double maxArea = areaArray[0];
        for (int i = 1; i < 3; i++) {
            System.out.printf("%d , %,.2f , %,.2f , %,.2f , %,.2f\n", c++, limelight.getTx(), limelight.getTy(), limelight.getTa(), limelight.getTv());
            if (areaArray[i] > maxArea) {
                longRectangleIndex = i;
                maxArea = areaArray[i];
            }
        }

        if (IsTeamRed && yArray[longRectangleIndex] < yArray[(longRectangleIndex + 1) % 3] && yArray[longRectangleIndex] < yArray[(longRectangleIndex + 2) % 3]) {
            return true;
        } else if ((!IsTeamRed) && yArray[longRectangleIndex] > yArray[(longRectangleIndex + 1) % 3] && yArray[longRectangleIndex] > yArray[(longRectangleIndex + 2) % 3]){
            return true;
        }
        return false;
        }
}
