import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="BasicTeleOp", group="TeleOp")
public class BasicTeleOp extends OpMode {

    private Servo armServo;
    private Servo intakeServo;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor mainArm;

    // Servo position tracking
    private double armServoPosition = 0.5; // Initial position (middle)
    private double intakeServoPosition = 0.5; // Initial position (middle)
    private final double SERVO_INCREMENT = 0.005; // Smaller increment for smooth movement

    @Override
    public void init() {
        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        mainArm = hardwareMap.get(DcMotor.class, "mainArm");

        // Initialize servos
        armServo = hardwareMap.get(Servo.class, "armServo");
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");

        // Set the right-side motors to reverse direction
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Joystick inputs for driving
        double y = -gamepad1.left_stick_y; // Forward/backward (negated to align joystick input with direction)
        double x = gamepad1.left_stick_x * 1.1; // Strafing (scaled for better control)
        double turn = gamepad1.right_stick_x; // Rotation

        // Arm control using right and left triggers
        double armPower = gamepad2.right_trigger - gamepad2.left_trigger;
        mainArm.setPower(armPower);

        // Mecanum drive calculations
        double leftFrontPower = y + x + turn;
        double rightFrontPower = y - x - turn;
        double leftRearPower = y - x + turn;
        double rightRearPower = y + x - turn;

        // Normalize powers if necessary
        double maxPower = Math.max(1.0, Math.max(
                Math.abs(leftFrontPower),
                Math.max(Math.abs(rightFrontPower), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)))
        ));

        leftFrontPower /= maxPower;
        rightFrontPower /= maxPower;
        leftRearPower /= maxPower;
        rightRearPower /= maxPower;

        // Set motor powers
        frontLeft.setPower(leftFrontPower);
        frontRight.setPower(rightFrontPower);
        backLeft.setPower(leftRearPower);
        backRight.setPower(rightRearPower);

        // Continuous servo adjustment based on button hold
        if (gamepad2.a) {
            armServoPosition = Math.min(armServoPosition + SERVO_INCREMENT, 1.0); // Increment while holding "a"
        }
        if (gamepad2.b) {
            armServoPosition = Math.max(armServoPosition - SERVO_INCREMENT, 0.0); // Decrement while holding "b"
        }
        if (gamepad2.x) {
            intakeServoPosition = Math.min(intakeServoPosition + SERVO_INCREMENT, 1.0); // Increment while holding "x"
        }
        if (gamepad2.y) {
            intakeServoPosition = Math.max(intakeServoPosition - SERVO_INCREMENT, 0.0); // Decrement while holding "y"
        }

        // Set servo positions
        if (armServo != null) {
            armServo.setPosition(armServoPosition);
        }
        if (intakeServo != null) {
            intakeServo.setPosition(intakeServoPosition);
        }

        // Telemetry for debugging
        telemetry.addData("Left Joystick (y)", y);
        telemetry.addData("Left Joystick (x)", x);
        telemetry.addData("Right Joystick (turn)", turn);
        telemetry.addData("Front Left Power", leftFrontPower);
        telemetry.addData("Front Right Power", rightFrontPower);
        telemetry.addData("Back Left Power", leftRearPower);
        telemetry.addData("Back Right Power", rightRearPower);
        telemetry.update();
    }
}
