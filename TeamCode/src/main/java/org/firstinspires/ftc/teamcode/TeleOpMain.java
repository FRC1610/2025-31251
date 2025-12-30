package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name="TeleOpMain", group = "Main")
public class TeleOpMain extends OpMode {
    private final RobotHardware robot = new RobotHardware();
    private double targetRpm = Constants.LAUNCHER_DEFAULT_RPM;
    private boolean launcherRunning = false;

    // Code to run ONCE when the driver hits INIT

    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    // Code to run REPEATEDLY after the driver hits START but before they hit STOP
    @Override
    public void loop() {
        // Drive the robot using the left stick for translation and the right stick for rotation.
        robot.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        // Run the intake forward with the right bumper, reverse with the left bumper, or stop if neither is pressed.
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

    private void handleLauncherControls() {
        // Toggle the launcher on/off with the Start button and apply the current target RPM when turning on.
        if (gamepad2.startWasPressed()) {
            launcherRunning = !launcherRunning;
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            } else {
                robot.launcher.setPower(0);
            }
        }

        // Increase target RPM with D-pad right; if running, update the launcher velocity immediately.
        if (gamepad2.dpadRightWasPressed()) {
            targetRpm += Constants.LAUNCHER_RPM_INCREMENT;
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            }
        }

        // Decrease target RPM with D-pad left; if running, update the launcher velocity immediately.
        if (gamepad2.dpadLeftWasPressed()) {
            targetRpm = Math.max(0, targetRpm - Constants.LAUNCHER_RPM_INCREMENT);
            if (launcherRunning) {
                robot.launcher.setVelocity(rpmToTicksPerSecond(targetRpm));
            }
        }
    }

    private void reportTelemetry() {
        // Send all telemetry to the driver station in a single update each loop.
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
