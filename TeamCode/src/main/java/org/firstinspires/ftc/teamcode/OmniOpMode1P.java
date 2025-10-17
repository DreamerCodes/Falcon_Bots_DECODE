package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Omni OpMode 1 Player", group="Linear OpMode")
//@Disabled
public class OmniOpMode1P extends LinearOpMode {
    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    //private DcMotor beltLaunchRight = null;
    //private DcMotor beltLaunchLeft = null;

    //private DcMotor intake = null;
    //private DcMotor sorter = null;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");

        //beltLaunchRight = hardwareMap.get(DcMotor.class, "belt_launch_right");
        //beltLaunchLeft = hardwareMap.get(DcMotor.class, "belt_launch_left");

        // intake = hardwareMap.get(DcMotor.class, "scoop");
        // sorter = hardwareMap.get(DcMotor.class, "sorter");

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        //beltLaunchRight.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //WHEELS//
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial - lateral - yaw;
            double rightFrontPower = axial + lateral + yaw;
            double leftBackPower = axial + lateral - yaw;
            double rightBackPower = axial - lateral + yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }
            
            else if (max < 0.01) {
                leftFrontDrive.setPower(0);
                leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rightFrontDrive.setPower(0);
                rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                leftBackDrive.setPower(0);
                leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rightBackDrive.setPower(0);
                rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);


            //LAUNCHER/BELT GUN//
            /*
            if (gamepad1.x) {
                beltLaunchLeft.setPower(1.0);
                beltLaunchRight.setPower(-1.0);
                telemetry.addData("Launcher", "shoot");
            }

            else {
                telemetry.addData("Launcher", "idle");

                beltLaunchLeft.setPower(0);
                beltLaunchLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                beltLaunchRight.setPower(0);
                beltLaunchRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            //SORTER//
            if (gamepad1.dpad_right) {
                sorter.setPower(.3);
                telemetry.addData("Sorter", "spinning right");
            }

            else if (gamepad1.dpad_left) {
                sorter.setPower(-.3);
                telemetry.addData("Sorter", "spinning left");
            }

            else {
                telemetry.addData("Sorter", "idle");
                sorter.setPower(0);
                sorter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            */

            //INTAKE//


            //TELEMETRY//
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }
    }
}
