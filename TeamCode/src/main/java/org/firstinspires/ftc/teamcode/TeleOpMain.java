package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.robotcore.util.AngleUnit;

@TeleOp(name="TeleOpMain", group = "Main")
public class TeleOpMain extends OpMode {
    private final RobotHardware robot = new RobotHardware();
    private double targetRpm = Constants.LAUNCHER_DEFAULT_RPM;
    private boolean launcherRunning = false;

    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;

    // Code to run ONCE when the driver hits INIT

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    // Code to run REPEATEDLY after the driver hits START but before they hit STOP
    @Override
    public void loop() {
        //Drive
        mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //Intake
        if (gamepad2.right_bumper){
            robot.intake.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.intake.setPower(Constants.INTAKE_FORWARD_SPEED);
        } else if (gamepad2.left_bumper) {
            robot.intake.setDirection(DcMotorSimple.Direction.REVERSE);
            robot.intake.setPower(Constants.INTAKE_REVERSE_SPEED);
        } else {
            robot.intake.setPower(0);
        }

        handleLauncherControls();
        reportTelemetry();
    }

    void mecanumDrive(double forward, double strafe, double rotate){

        /* the denominator is the largest motor power (absolute value) or 1
         * This ensures all the powers maintain the same ratio,
         * but only if at least one is out of the range [-1, 1]
         */
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate), 1);

        leftFrontPower = (forward + strafe + rotate) / denominator;
        rightFrontPower = (forward - strafe - rotate) / denominator;
        leftBackPower = (forward - strafe + rotate) / denominator;
        rightBackPower = (forward + strafe - rotate) / denominator;

        robot.leftFrontDrive.setPower(leftFrontPower);
        robot.rightFrontDrive.setPower(rightFrontPower);
        robot.leftBackDrive.setPower(leftBackPower);
        robot.rightBackDrive.setPower(rightBackPower);

    }

    private void handleLauncherControls() {
        if (gamepad2.startWasPressed()) {
            launcherRunning = !launcherRunning;
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            } else {
                robot.launcher.setPower(0);
            }
        }

        gamepad2.startWasReleased();

        if (gamepad2.dpadRightWasPressed()) {
            targetRpm += Constants.LAUNCHER_RPM_INCREMENT;
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            }
        }

        if (gamepad2.dpadLeftWasPressed()) {
            targetRpm = Math.max(0, targetRpm - Constants.LAUNCHER_RPM_INCREMENT);
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            }
        }
    }

    private void reportTelemetry() {
        addLauncherTelemetry();
        addImuTelemetry();
        telemetry.update();
    }

    private void addLauncherTelemetry() {
        double currentVelocityTicksPerSecond = robot.launcher.getVelocity();
        double currentRpm = ticksPerSecondToRpm(currentVelocityTicksPerSecond);

        telemetry.addData("Launcher Target RPM", targetRpm);
        telemetry.addData("Launcher Current RPM", currentRpm);
        telemetry.addData("Launcher Running", launcherRunning);
    }

    private void addImuTelemetry() {
        YawPitchRollAngles orientation = robot.imu.getRobotYawPitchRollAngles();

        telemetry.addData("IMU Heading (deg)", orientation.getYaw(AngleUnit.DEGREES));
        telemetry.addData("IMU Pitch (deg)", orientation.getPitch(AngleUnit.DEGREES));
        telemetry.addData("IMU Roll (deg)", orientation.getRoll(AngleUnit.DEGREES));
    }

    private double rpmToTicksPerSecond(double rpm) {
        return rpm * Constants.REV_HD_HEX_TICKS_PER_REV / 60.0;
    }

    private double ticksPerSecondToRpm(double ticksPerSecond) {
        return (ticksPerSecond / Constants.REV_HD_HEX_TICKS_PER_REV) * 60.0;
    }
}
