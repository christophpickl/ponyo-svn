package at.ac.e0525580.kinect.first.official;

import com.libfreenect.*;

public class App {
	public static void main(String[] args) {

        System.out.println("START");

        System.out.println("get device");
        final KinectDevice device = KinectDevice.createInstance();
        
        
        System.out.println("red");
        device.getLed().setStatus(LEDStatus.RED);
        try { device.getMotor().setPosition(0.0); } catch (MotorPositionOutOfBounds e) { e.printStackTrace(); }

        System.out.println("sleeping");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
//
//        System.out.println("green");
//        device.getLed().setStatus(LEDStatus.GREEN);
//        try { device.getMotor().setPosition(1.0); } catch (MotorPositionOutOfBounds e) { e.printStackTrace(); }
//        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }


        System.out.println("close");
//        try { device.getMotor().setPosition(0.0); } catch (MotorPositionOutOfBounds e) { e.printStackTrace(); }
        
        
        device.close();
        
        System.out.println("END");
        System.exit(0);
	}
}
