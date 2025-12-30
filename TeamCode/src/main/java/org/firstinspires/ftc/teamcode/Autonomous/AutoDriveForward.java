package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotHardware;

@Autonomous(name = "AutoDriveForward", group = "Auto")
public class AutoDriveForward extends LinearOpMode {
    private final RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware, set up encoders, and share starting telemetry before waiting for Start.
        robot.init(hardwareMap);
        robot.resetDriveEncoders();

        telemetry.addData("Target Distance (in)", Constants.AUTO_DRIVE_FORWARD_DISTANCE);
        telemetry.addData("Drive Power", Constants.AUTO_DRIVE_POWER);
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Command the drivetrain to move forward the target distance at the configured power.
            driveForward(Constants.AUTO_DRIVE_FORWARD_DISTANCE, Constants.AUTO_DRIVE_POWER);
        }

        // Restore normal encoder usage after autonomous movement completes.
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void driveForward(double distanceInches, double power) {
        // Convert the requested distance in inches to encoder ticks for all four drive motors.
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
        while (opModeIsActive() && anyDriveMotorBusy()) {
            telemetry.addData("Target Ticks", targetTicks);
            telemetry.addData("LF Position", robot.leftFrontDrive.getCurrentPosition());
            telemetry.addData("RF Position", robot.rightFrontDrive.getCurrentPosition());
            telemetry.addData("LB Position", robot.leftBackDrive.getCurrentPosition());
            telemetry.addData("RB Position", robot.rightBackDrive.getCurrentPosition());
            telemetry.update();
            idle();
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
