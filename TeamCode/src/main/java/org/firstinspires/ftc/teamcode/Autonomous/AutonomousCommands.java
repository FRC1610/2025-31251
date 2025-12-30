package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotHardware;

/**
 * Reusable helper routines for autonomous op modes.
 */
public class AutonomousCommands {
    private final LinearOpMode opMode;
    private final RobotHardware robot;

    public AutonomousCommands(LinearOpMode opMode, RobotHardware robot) {
        this.opMode = opMode;
        this.robot = robot;
    }

    public void driveForward(double distanceInches, double power) {
        // Convert requested distance in inches to encoder ticks for all four drive motors.
        int targetTicks = (int) Math.round(distanceInches * Constants.DRIVE_TICKS_PER_INCH);

        robot.leftFrontDrive.setTargetPosition(targetTicks);
        robot.rightFrontDrive.setTargetPosition(targetTicks);
        robot.leftBackDrive.setTargetPosition(targetTicks);
        robot.rightBackDrive.setTargetPosition(targetTicks);

        // Use RUN_TO_POSITION so each motor stops itself once it reaches the target.
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Apply forward power using the shared mecanum helper (strafe/turn set to zero).
        robot.mecanumDrive(power, 0, 0);

        // Keep reporting progress while any motor is still trying to reach its target.
        while (opMode.opModeIsActive() && anyDriveMotorBusy()) {
            opMode.telemetry.addData("Target Ticks", targetTicks);
            opMode.telemetry.addData("LF Position", robot.leftFrontDrive.getCurrentPosition());
            opMode.telemetry.addData("RF Position", robot.rightFrontDrive.getCurrentPosition());
            opMode.telemetry.addData("LB Position", robot.leftBackDrive.getCurrentPosition());
            opMode.telemetry.addData("RB Position", robot.rightBackDrive.getCurrentPosition());
            opMode.telemetry.update();
            opMode.idle();
        }

        // Stop all drive power once the motion is complete or the op mode ends.
        robot.mecanumDrive(0, 0, 0);
    }

    private boolean anyDriveMotorBusy() {
        // Check whether any drive motor is still moving toward its target position.
        return robot.leftFrontDrive.isBusy()
                || robot.rightFrontDrive.isBusy()
                || robot.leftBackDrive.isBusy()
                || robot.rightBackDrive.isBusy();
    }
}
