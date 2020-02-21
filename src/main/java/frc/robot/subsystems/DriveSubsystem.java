package frc.robot.subsystems;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.Encoder;
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
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

public class DriveSubsystem extends SubsystemBase {
    private static final double MAX_SPEED = 2;

    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 12 / MAX_SPEED);

    private DifferentialDrive driveBase;
    private DifferentialDriveOdometry odometry;

    private CANEncoder leftEncoder;
    private CANEncoder rightEncoder;

    private PIDController leftController;
    private PIDController rightController;

    private Gyro gyro;

    private DifferentialDriveKinematics kin = new DifferentialDriveKinematics(0.76);

    public DriveSubsystem(Pose2d initialPosMeters) {
        this(Motors.leftDrive, Motors.rightDrive, Sensors.driveEncoderLeft, Sensors.driveEncoderRight, Sensors.gyro, initialPosMeters);
    }

    private DriveSubsystem(SpeedController left, SpeedController right, CANEncoder eLeft, CANEncoder eRight, Gyro gyro, Pose2d initialPosMeters) {
        super();
        driveBase = new DifferentialDrive(left, right);

        // Assumes encoders measure rate as m/s for each side
        leftEncoder = eLeft;
        rightEncoder = eRight;

        leftController = new PIDController(1, 0, 0);
        rightController = new PIDController(1, 0, 0);

        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);

        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(-gyro.getAngle()), initialPosMeters);
    }

    public DifferentialDrive getDriveBase() {
        return driveBase;
    }

    @Override
    public void periodic() {
        odometry.update(Rotation2d.fromDegrees(-gyro.getAngle()), leftEncoder.getPosition(), rightEncoder.getPosition());
    }

    public RamseteCommand ramseteCommandBuilder(Trajectory t) {
        return new RamseteCommand(
                t,
                odometry::getPoseMeters,
                new RamseteController(),
                feedforward,
                kin,
                () -> new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity()),
                leftController,
                rightController,
                (vLeft, vRight) -> {
                    Motors.leftDrive.setVoltage(vLeft);
                    Motors.rightDrive.setVoltage(vRight);
                },
                this
        );
    }
}