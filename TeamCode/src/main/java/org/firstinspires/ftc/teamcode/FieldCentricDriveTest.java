package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "FieldCentricDriveTest", group = "Test")
public class FieldCentricDriveTest extends OpMode {
    private final RobotHardware robot = new RobotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);

        robot.imu.resetYaw();

        telemetry.addData("Status", "Initialized - Yaw zeroed for field centric drive");
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        robot.fieldCentricDrive(forward, strafe, rotate);

        addDriveTelemetry();
    }

    private void addDriveTelemetry() {
        YawPitchRollAngles orientation = robot.imu.getRobotYawPitchRollAngles();
        double headingDegrees = orientation.getYaw(AngleUnit.DEGREES);

        telemetry.addData("Heading (deg)", headingDegrees);
        telemetry.addData("Forward Input", -gamepad1.left_stick_y);
        telemetry.addData("Strafe Input", gamepad1.left_stick_x);
        telemetry.addData("Rotate Input", gamepad1.right_stick_x);
        telemetry.update();
    }
}
