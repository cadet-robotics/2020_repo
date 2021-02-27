package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

import static frc.robot.Constants.MAX_SPEED;

public class DriveSubsystem extends SubsystemBase {
    private static final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(3 / MAX_SPEED, 16 / MAX_SPEED);

    private static final DifferentialDriveKinematics kin = new DifferentialDriveKinematics(0.64);

    private DifferentialDrive driveBase;

    private CANEncoder leftEncoder;
    private CANEncoder rightEncoder;

    private PIDController leftController = new PIDController(0.5, 0, 0);
    private PIDController rightController = new PIDController(0.5, 0, 0);

    private DifferentialDriveOdometry odometry;

    private Gyro gyro;

    public DriveSubsystem(Pose2d initialPosMeters) {
        this(Motors.leftDrive, Motors.rightDrive, Sensors.driveEncoderLeft, Sensors.driveEncoderRight, Sensors.gyro, initialPosMeters);
    }

    private DriveSubsystem(SpeedController left, SpeedController right, CANEncoder eLeft, CANEncoder eRight, Gyro gyroIn, Pose2d initialPosMeters) {
        super();
        driveBase = new DifferentialDrive(left, right);
        //driveBase.setRightSideInverted(true);

        // Assumes encoders measure rate as m/s for each side
        leftEncoder = eLeft;
        rightEncoder = eRight;

        gyro = gyroIn;
    }

    public DifferentialDrive getDriveBase() {
        return driveBase;
    }

    @Override
    public void periodic() {
        if (odometry != null) {
            odometry.update(Rotation2d.fromDegrees(Sensors.getGyro()), leftEncoder.getPosition(), rightEncoder.getPosition());
        }
    }

    public Command trajectoryCommandBuilder(Trajectory t, Pose2d initialPosMeters) {


        t.getStates().forEach(System.out::println);

        return new InstantCommand(() -> {
            leftEncoder.setPosition(0);
            rightEncoder.setPosition(0);

            leftController.reset();
            rightController.reset();

            odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(Sensors.getGyro()), initialPosMeters);
        }).andThen(new RamseteCommand(
                t,
                () -> {
                    Pose2d o = odometry.getPoseMeters();
                    //SmartDashboard.putNumber("Gyro 2 Electric Boogaloo", o.getRotation().getDegrees());
                    SmartDashboard.putString("Position 2 Electric Boogaloo", o.toString());
                    //System.out.println(":> " + o);
                    return o;
                },
                new RamseteController(2.0, 0.7),
                feedforward,
                kin,
                () -> new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity()),
                leftController,
                rightController,
                (vLeft, vRight) -> {
                    double vMain = RobotController.getBatteryVoltage();
                    //System.out.println("->" + vMain);
                    SmartDashboard.putNumber("Motor Left Volt", vLeft);
                    SmartDashboard.putNumber("Motor Right Volt", vRight);
                    //System.out.println(leftController.getPositionError() + " ||| " + rightController.getPositionError());
                    driveBase.tankDrive(vLeft / vMain, vRight / vMain, false);
                },
                this
        )).andThen(new InstantCommand(() -> {
            System.out.println("End Pos: " + odometry.getPoseMeters());
            odometry = null;
        }));
    }
}