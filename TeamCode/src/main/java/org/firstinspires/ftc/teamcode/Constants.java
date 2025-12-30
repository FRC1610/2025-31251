package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public final class Constants {
    public static final double LAUNCHER_DEFAULT_RPM = 3000.0;
    public static final double LAUNCHER_RPM_INCREMENT = 100.0;
    public static final PIDFCoefficients LAUNCHER_PIDF = new PIDFCoefficients(30.0, 0.0, 5.0, 12.0);
    public static final double REV_HD_HEX_TICKS_PER_REV = 560.0;
    public static final double INTAKE_FORWARD_SPEED = 1.0;
    public static final double INTAKE_REVERSE_SPEED = 1.0;
    public static final String IMU_NAME = "imu";
    public static final RevHubOrientationOnRobot.LogoFacingDirection IMU_LOGO_FACING_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
    public static final RevHubOrientationOnRobot.UsbFacingDirection IMU_USB_FACING_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.UP;

    private Constants() {
        // Utility class
    }
}
