package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotHardware;

@Autonomous(name = "AutoDriveForward", group = "Auto")
public class AutoDriveForward extends LinearOpMode {
    private final RobotHardware robot = new RobotHardware();
    private AutonomousCommands autonomousCommands;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware, set up encoders, and share starting telemetry before waiting for Start.
        robot.init(hardwareMap);
        autonomousCommands = new AutonomousCommands(this, robot);
        robot.resetDriveEncoders();

        telemetry.addData("Target Distance (in)", Constants.AUTO_DRIVE_FORWARD_DISTANCE);
        telemetry.addData("Drive Power", Constants.AUTO_DRIVE_POWER);
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Command the drivetrain to move forward the target distance at the configured power.
            autonomousCommands.driveForward(Constants.AUTO_DRIVE_FORWARD_DISTANCE, Constants.AUTO_DRIVE_POWER);
        }

        // Restore normal encoder usage after autonomous movement completes.
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
