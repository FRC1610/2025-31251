package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public final class Constants {
    // Launcher settings
    public static final double LAUNCHER_DEFAULT_RPM = 3000.0;
    public static final double LAUNCHER_RPM_INCREMENT = 100.0;
    public static final PIDFCoefficients LAUNCHER_PIDF = new PIDFCoefficients(30.0, 0.0, 0.0, 12.0);

    // Drive and wheel measurements
    public static final double REV_HD_HEX_TICKS_PER_REV = 560.0;
    public static final double YELLOW_JACKET_19_2_TICKS_PER_REV = 537.6;
    public static final double GRIPFORCE_MECANUM_DIAMETER_MM = 104.0;
    public static final double MILLIMETERS_PER_INCH = 25.4;
    public static final double GRIPFORCE_MECANUM_DIAMETER_INCHES =
            GRIPFORCE_MECANUM_DIAMETER_MM / MILLIMETERS_PER_INCH;
    public static final double GRIPFORCE_MECANUM_CIRCUMFERENCE_INCHES =
            Math.PI * GRIPFORCE_MECANUM_DIAMETER_INCHES;
    public static final double DRIVE_TICKS_PER_INCH =
            YELLOW_JACKET_19_2_TICKS_PER_REV / GRIPFORCE_MECANUM_CIRCUMFERENCE_INCHES;

    // Autonomous configuration
    public static final double AUTO_DRIVE_FORWARD_DISTANCE = 18.0;
    public static final double AUTO_DRIVE_POWER = 0.5;

    // Intake settings
    public static final double INTAKE_FORWARD_SPEED = 1.0;
    public static final double INTAKE_REVERSE_SPEED = 1.0;

    // IMU configuration
    public static final String IMU_NAME = "imu";
    public static final RevHubOrientationOnRobot.LogoFacingDirection IMU_LOGO_FACING_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
    public static final RevHubOrientationOnRobot.UsbFacingDirection IMU_USB_FACING_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.UP;

    private Constants() {
        // Utility class
    }
}
