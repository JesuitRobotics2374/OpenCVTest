import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


import java.util.*;
import java.util.stream.Collectors;

public class OpenCVTest {
    //THIS DOES NOT WORK YET - TESTING
    //ASSUMPTIONS:
    // Hands will generally be smaller in size compared to the shoulder pads.
    // The aspect ratio of the hands will be different from the shoulder pads.
    // Two hands and two shoulders will be in frame at all times.

    public static boolean AutonomousControl() {
        VideoCapture video = new VideoCapture(0);
        video.set(10, 0.5);
        while (true) {
            Mat newFrame = new Mat();
            video.read(newFrame);
            if (newFrame.empty()) {
                System.err.println("No frames to view.");
                continue;
            }
            
            // convert to hsv and get ready  to mask
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(newFrame, hsvFrame, Imgproc.COLOR_BGR2HSV);
            Scalar lowerColor = new Scalar(75, 200, 200);
            Scalar upperColor = new Scalar(85, 255, 255); // adjust to liking
            
            // mask
            Mat mask = new Mat();
            Mat maskFrame = new Mat();
            Core.inRange(hsvFrame, lowerColor, upperColor, mask);
            Core.bitwise_and(newFrame, newFrame, mask, maskFrame);

            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // filter out small contours (possible noise)
            double minContourArea = 100;  // adjust to liking
            List<MatOfPoint> filteredContours = contours.stream()
                .filter(contour -> Imgproc.contourArea(contour) > minContourArea)
                .collect(Collectors.toList());

            // Validate the number of contours
            if (filteredContours.size() != 4) {
                  continue;  // skip to next frame
            }

            // Validate that no contour is much larger than the others
            double maxContourArea = filteredContours.stream()
                .mapToDouble(Imgproc::contourArea)
                .max()
                .orElse(0);

            double avgContourArea = filteredContours.stream()
                .mapToDouble(Imgproc::contourArea)
                .average()
                .orElse(0);

            if (maxContourArea > 2 * avgContourArea) {  // Adjust the factor '2' as needed ( how many times bigger than others maximum )
                continue;  // skip to next frame
            }
            
            Point rightHand = new Point();
            Point leftHand  = new Point();
            Point rightShoulder = new Point();
            Point leftShoulder = new Point();
            
            // Store contour centers and labels temporarily
            List<Point> centers = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            
            for (MatOfPoint contour : filteredContours) {

                Rect rect = Imgproc.boundingRect(contour);
                Point center = new Point(rect.x + rect.width * 0.5, rect.y + rect.height * 0.5);
                centers.add(center);
            
                double contourArea = Imgproc.contourArea(contour);
                double aspectRatio = (double) rect.width / rect.height;
            
                // Differentiate between hands and shoulders based on area and aspect ratio
                String label;
                if (contourArea < avgContourArea && aspectRatio < 1.5) { // ADJUST
                    label = "H";
                } else {
                    label = "S";
                }
                labels.add(label);
            }
            
            // sort centers based on x-coordinates and label them
            for (int i = 0; i < centers.size(); i++) {
                Point center = centers.get(i);
                String label = labels.get(i);
                if (i < centers.size() / 2) {
                    if (label.equals("H")) {
                        leftHand = center;
                    } else {
                        leftShoulder = center;
                    }
                } else {
                    if (label.equals("H")) {
                        rightHand = center;
                    } else {
                        rightShoulder = center;
                    }
                }
            }

            Point rightVector = new Point(rightHand.x - rightShoulder.x, rightHand.y - rightShoulder.y);
            Point leftVector = new Point(leftHand.x - leftShoulder.x, leftHand.y - leftShoulder.y);

            // horizontal reference vector
            Point reference = new Point(1, 0);

            // right side
            double dotProductRight = rightVector.x * reference.x + rightVector.y * reference.y;
            double magnitudeRight = Math.sqrt(rightVector.x * rightVector.x + rightVector.y * rightVector.y);
            double cosThetaRight = dotProductRight / magnitudeRight;
            double angleRight = Math.acos(cosThetaRight);

            // adjusting the sign of the angle based on the cross product
            double crossProductRight = rightVector.x * reference.y - rightVector.y * reference.x;
            if (crossProductRight < 0) {
                angleRight = -angleRight;
            }

            // left side
            double dotProductLeft = leftVector.x * reference.x + leftVector.y * reference.y;
            double magnitudeLeft = Math.sqrt(leftVector.x * leftVector.x + leftVector.y * leftVector.y);
            double cosThetaLeft = dotProductLeft / magnitudeLeft;
            double angleLeft = Math.acos(cosThetaLeft);

            // Adjusting the sign of the angle based on the cross product to get the angle in the range [pi/2, -pi/2]
            double crossProductLeft = leftVector.x * reference.y - leftVector.y * reference.x;
            if (crossProductLeft < 0) {
                angleLeft = -angleLeft;
            }

            double FINALR = angleRight / (Math.PI / 2D);
            double FINALL = angleLeft / (Math.PI / 2D);
	System.out.println("RIGHT: " + FINALR);
	System.out.println("LEFT: " + FINALL);
        }
    }
}

