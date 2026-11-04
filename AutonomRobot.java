package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

    @Autonomous(name = "AutonomRobot", group = "Competition")
    public class AutonomRobot extends OpMode {

        public enum State {
            INITIAL,
            DRIVE_FORWARD,
            LOWER_ARM,
            CHECK_SENSOR,
            GRAB_ELEMENT,
            DRIVE_BACKWARD,
            IDLE
        }

        private DcMotorEx leftMotor;
        private DcMotorEx rightMotor;
        private DcMotorEx armMotor;
        private Servo clawServo;
        private TouchSensor touch;

        private State currentState = State.INITIAL;
        ElapsedTime timer = new ElapsedTime();

        public void init() {
            leftMotor = hardwareMap.get(DcMotorEx.class, "motor_left");
            rightMotor = hardwareMap.get(DcMotorEx.class, "motor_right");
            armMotor = hardwareMap.get(DcMotorEx.class, "motor_arm");
            clawServo = hardwareMap.get(Servo.class, "claw");
            touch = hardwareMap.get(TouchSensor.class, "touch");

            leftMotor.setDirection(DcMotorEx.Direction.FORWARD);
            rightMotor.setDirection(DcMotorEx.Direction.REVERSE);

            armMotor.setTargetPosition(1440);
            armMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.6);

            clawServo.setPosition(1.0);
        }
        private void setDrivePower(double p){
            leftMotor.setPower(p);
            rightMotor.setPower(p);
        }

        public void loop() {

            switch (currentState) {

                case INITIAL:
                    currentState = State.DRIVE_FORWARD;
                    break;

                case DRIVE_FORWARD:
                    timer.reset();
                    setDrivePower(0.5);
                    if (timer.seconds() >= 4.0) {
                        stopDrive();
                        currentState = State.LOWER_ARM;
                    }
                    break;

                case LOWER_ARM:
                    if (!armMotor.isBusy()) {
                        currentState = State.CHECK_SENSOR;
                    }
                    break;

                case CHECK_SENSOR:
                    if (touch.isPressed()) {
                        currentState = State.GRAB_ELEMENT;
                    }else{
                        currentState = State.DRIVE_BACKWARD;
                    }
                    break;

                case GRAB_ELEMENT:
                    clawServo.setPosition(0.0);
                    currentState = State.DRIVE_BACKWARD;
                    break;

                case DRIVE_BACKWARD:
                    timer.reset();
                    setDrivePower(-0.5);
                    if (timer.seconds() >= 6.0) {
                        stopDrive();
                        currentState = State.IDLE;
                    }
                    break;

                case IDLE:
                    stopDrive();
                    break;
            }
        }

        private void stopDrive() {
            setDrivePower(0);
        }
    }
