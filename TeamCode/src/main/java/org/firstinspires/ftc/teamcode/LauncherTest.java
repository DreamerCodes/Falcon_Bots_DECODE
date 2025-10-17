package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Launcher Test")
public class LauncherTest extends OpMode {
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor beltLaunchRight = null;
    private DcMotor beltLaunchLeft = null;

    double launcherPower = 0.0;
    String launcherStatus = "off";

    // Track previous D-pad states to detect single presses
    boolean lastDpadUp = false;
    boolean lastDpadDown = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");

        // Initialize both motors from configuration
        beltLaunchRight = hardwareMap.get(DcMotor.class, "belt_launch_right");
        beltLaunchLeft  = hardwareMap.get(DcMotor.class, "belt_launch_left");

        // Set directions (adjust as needed for your setup)
        beltLaunchRight.setDirection(DcMotor.Direction.REVERSE);
        beltLaunchLeft.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // A & B buttons for on/off control
        if (gamepad1.a) {
            launcherPower = 0.1;
            launcherStatus = "on";
        }
        if (gamepad1.b) {
            launcherPower = 0.0;
            launcherStatus = "off";
        }

        // D-pad increment/decrement with single press detection
        if (gamepad1.dpad_up && !lastDpadUp && launcherPower < 1.0) {
            launcherPower += 0.1;
        }
        if (gamepad1.dpad_down && !lastDpadDown && launcherPower > 0.0) {
            launcherPower -= 0.1;
        }

        // Clamp power to valid range (0.0 to 1.0)
        launcherPower = Math.max(0.0, Math.min(1.0, launcherPower));

        // Update previous button states
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;

        // Apply power to both motors
        beltLaunchRight.setPower(launcherPower);
        beltLaunchLeft.setPower(launcherPower);

        // Show runtime and launcher info
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Launcher Status", launcherStatus);
        telemetry.addData("Launcher Power", launcherPower);
    }

    @Override
    public void stop() {
        beltLaunchRight.setPower(0);
        beltLaunchLeft.setPower(0);
    }
}
