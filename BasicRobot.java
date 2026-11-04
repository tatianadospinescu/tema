package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="BasicRobot", group="Competition")
public class BasicRobot extends OpMode {
    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    Servo armServo;
    Servo clawServo;

    @Override public void init(){
        leftMotor = hardwareMap.get(DcMotorEx.class, "motor_left");
        rightMotor = hardwareMap.get(DcMotorEx.class, "motor_right");

        armServo = hardwareMap.get(Servo.class, "arm_pivot");
        clawServo = hardwareMap.get(Servo.class, "claw");

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        armServo.setPosition(0.0);
        clawServo.setPosition(0.0);
    }

    @Override public void loop(){
        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        double leftPower = Range.clip(drive + turn, -1.0, 1.0);
        double rightPower = Range.clip (drive - turn, -1.0, 1.0);
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        if (gamepad1.dpad_up) {
            armServo.setPosition(0.85);
        }else if(gamepad1.dpad_down){
            armServo.setPosition(0.2);
        }
        if (gamepad1.a){
            clawServo.setPosition(1.0);
        }else{
            clawServo.setPosition(0.0);
        }
        double clawPos = clawServo.getPosition();

        telemetry.addData ("Putere Stanga", leftMotor.getPower());
        telemetry.addData ("Putere Dreapta", rightMotor.getPower());
        telemetry.addData ("Pozitie Brat", armServo.getPosition());
        if (clawPos == 1.0){
            telemetry.addData("Gheara", "DESCHISA");
        }else if (clawPos == 0.0){
            telemetry.addData("Gheara", "INCHISA");
        }
        telemetry.update();
    }
}
