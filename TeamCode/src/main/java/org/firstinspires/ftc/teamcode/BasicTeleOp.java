import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="BasicTeleOp", group="TeleOp")
public class BasicTeleOp extends OpMode {

    private Servo armServo = null;
    private Servo intakeServo = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor mainArm;

    // Runs once when the OpMode starts
    @Override
    public void init() {
        // Map the motors to their configuration names in the Control Hub
        frontLeft = hardwareMap.get(DcMotor.class,"frontLeft");
        frontRight = hardwareMap.get(DcMotor.class,"frontRight");
        backLeft = hardwareMap.get(DcMotor.class,"backLeft");
        backRight = hardwareMap.get(DcMotor.class,"backRight");
        mainArm = hardwareMap.get(DcMotor.class, "mainArm");

        // Servos
      //  armServo = hardwareMap.get(Servo.class, "armServo");
      // intakeServo = hardwareMap.get(Servo.class, "intakeServo");

        // Set the right-side motors to reverse direction
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        mainArm.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop() {
        // Get joystick inputs
        double y = -gamepad1.left_stick_y; // Forward/backward movement
        double x = gamepad1.left_stick_x * 1.1; // Strafing movement, adjusted for more accurate strafing
        double turn = gamepad1.right_stick_x; // Rotation
        // Arm control using RT and LT triggers
        double armPower = gamepad1.right_trigger - gamepad1.left_trigger;
        mainArm.setPower(armPower);

        // Calculate motor powers based on Mecanum drive formulas
        double leftFrontPower = y + x + turn;
        double rightFrontPower = y - x - turn;
        double leftRearPower = y - x + turn;
        double rightRearPower = y + x - turn;

        // Normalize powers if any power is outside the range [-1, 1]
        double maxPower = Math.max(1.0, Math.max(
                Math.abs(leftFrontPower),
                Math.max(Math.abs(rightFrontPower), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)))
        ));

        leftFrontPower /= maxPower;
        rightFrontPower /= maxPower;
        leftRearPower /= maxPower;
        rightRearPower /= maxPower;

        // Set the motor powers
        frontLeft.setPower(leftFrontPower);
        frontRight.setPower(rightFrontPower);
        backLeft.setPower(leftRearPower);
        backRight.setPower(rightRearPower);

        // Servo control
        if (gamepad1.a) {
      //      armServo.setPosition(1.0); // Fully open the servo
        } else if (gamepad1.b) {
      //      armServo.setPosition(0.0); // Fully close the servo
        }
        if (gamepad1.x) {
       //     intakeServo.setPosition(1.0); // Fully open the servo
        } else if (gamepad1.y) {
        //    intakeServo.setPosition(0.0); // Fully close the servo
        }
    }
}
