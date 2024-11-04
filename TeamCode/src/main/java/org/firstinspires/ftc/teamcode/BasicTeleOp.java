import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="BasicTeleOp", group="TeleOp")
public class BasicTeleOp extends OpMode {

    // Declare motor variables for drive and arm
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor mainArm;

    @Override
    public void init() {
        // Initialize the motors using the hardware map
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        mainArm = hardwareMap.get(DcMotor.class, "mainArm");

        // Set the direction for the motors, adjust if needed for your setup
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        mainArm.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop() {
        // Tank drive control using left and right joysticks
        double leftPower = -gamepad1.left_stick_y; // Left side power (forward/backward)
        double rightPower = -gamepad1.right_stick_y; // Right side power (forward/backward)
        // Arm control using RT and LT triggers
        double armPower = gamepad1.right_trigger - gamepad1.left_trigger;
        mainArm.setPower(armPower);

        // Set power to each motor
        frontLeft.setPower(leftPower);
        backLeft.setPower(rightPower);
        frontRight.setPower(rightPower);
        backRight.setPower(leftPower);

        // Telemetry data to show motor powers on the driver station
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
        telemetry.addData("Arm Power", armPower);
        telemetry.update();
    }
}
