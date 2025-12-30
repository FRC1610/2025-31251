package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "FlywheelPidfTuner", group = "Test")
public class FlywheelPidfTuner extends OpMode {
    private final RobotHardware robot = new RobotHardware();

    private double targetRpm = Constants.LAUNCHER_DEFAULT_RPM;
    private boolean launcherRunning = false;

    private double pCoefficient = Constants.LAUNCHER_PIDF.p;
    private double fCoefficient = Constants.LAUNCHER_PIDF.f;

    @Override
    public void init() {
        robot.init(hardwareMap);
        applyPidfCoefficients();

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        // Allow the co-driver to toggle the flywheel and adjust RPM just like in TeleOpMain.
        handleLauncherControls();

        // Use gamepad1 to tune PIDF: P with dpad up/down and feedforward with dpad left/right.
        handlePidfTuning();

        // Share the current tuning values and launcher state each cycle.
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

    private void handlePidfTuning() {
        // Adjust feedforward with gamepad1 D-pad left/right in 0.25 increments.
        if (gamepad1.dpadRightWasPressed()) {
            fCoefficient += 0.25;
            applyPidfCoefficients();
        }

        if (gamepad1.dpadLeftWasPressed()) {
            fCoefficient = Math.max(0, fCoefficient - 0.25);
            applyPidfCoefficients();
        }

        // Adjust proportional gain with gamepad1 D-pad up/down in 0.25 increments.
        if (gamepad1.dpadUpWasPressed()) {
            pCoefficient += 0.25;
            applyPidfCoefficients();
        }

        if (gamepad1.dpadDownWasPressed()) {
            pCoefficient = Math.max(0, pCoefficient - 0.25);
            applyPidfCoefficients();
        }
    }

    private void applyPidfCoefficients() {
        // Apply the updated PIDF values with I and D set to zero for tuning simplicity.
        PIDFCoefficients updatedCoefficients = new PIDFCoefficients(pCoefficient, 0.0, 0.0, fCoefficient);
        robot.launcher.setPIDFCoefficients(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER, updatedCoefficients);
    }

    private void reportTelemetry() {
        double currentVelocityTicksPerSecond = robot.launcher.getVelocity();
        double currentRpm = ticksPerSecondToRpm(currentVelocityTicksPerSecond);

        telemetry.addData("Launcher Target RPM", targetRpm);
        telemetry.addData("Launcher Current RPM", currentRpm);
        telemetry.addData("Launcher Running", launcherRunning);
        telemetry.addData("P Gain", pCoefficient);
        telemetry.addData("F Gain", fCoefficient);
        telemetry.update();
    }

    private double rpmToTicksPerSecond(double rpm) {
        return rpm * Constants.REV_HD_HEX_TICKS_PER_REV / 60.0;
    }

    private double ticksPerSecondToRpm(double ticksPerSecond) {
        return (ticksPerSecond / Constants.REV_HD_HEX_TICKS_PER_REV) * 60.0;
    }
}
