package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;

public class DefaultDriveCommand extends CommandBase {
    private final DrivetrainSubsystem m_drivetrainSubsystem;

    private final DoubleSupplier m_translationXSupplier;
    private final DoubleSupplier m_translationYSupplier;
    private final DoubleSupplier m_rotationSupplier;

    public DefaultDriveCommand(DrivetrainSubsystem drivetrainSubsystem,
            DoubleSupplier translationXSupplier,
            DoubleSupplier translationYSupplier,
            DoubleSupplier rotationSupplier) {
        this.m_drivetrainSubsystem = drivetrainSubsystem;
        this.m_translationXSupplier = translationXSupplier;
        this.m_translationYSupplier = translationYSupplier;
        this.m_rotationSupplier = rotationSupplier;

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of
        // field-oriented movement
        // m_drivetrainSubsystem.drive(
        // ChassisSpeeds.fromFieldRelativeSpeeds(
        // (square(m_translationXSupplier.getAsDouble(),2)),
        // (square(m_translationYSupplier.getAsDouble(),2)),
        // (square(m_rotationSupplier.getAsDouble(),3)),
        // m_drivetrainSubsystem.getGyroscopeRotation()));

        //  Reduce the overall speed of robot
        //  - start with 50% (.5)
        //  - left trigger returns between 0 - 1 multiple it by .5 (pressing returns value > 0 *.5)
        //  - right trigger returns between 0 - 1 multiple it by .5 (pressing returns value > 0 *.5)
        //
        //  Use Case :
        //     Driving across field - speed up : 
        //     - Hold LT down which returns 1 * .5 = .5 speed add to .5 start is 1.0 speed
        //
        //     Driving up to cargo - slow down : 
        //     - Hold RT down which returns 1 * .5 = .5 speed subtract from 1 is .5 speed * .5 = .25 speed


        double driveSpeed = (.5+(RobotContainer.getInstance().getDriveController().getLeftTriggerAxis()*.5))*(1-(RobotContainer.getInstance().getDriveController().getRightTriggerAxis()*.5));
    
            m_drivetrainSubsystem.drive(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                        (m_translationXSupplier.getAsDouble())*driveSpeed,
                        (m_translationYSupplier.getAsDouble())*driveSpeed,
                        (m_rotationSupplier.getAsDouble())*driveSpeed,
                        m_drivetrainSubsystem.getGyroscopeRotation()));
        
        // SmartDashboard.putNumber("driveSpeed", driveSpeed);
        
        // new ChassisSpeeds(
        // m_translationXSupplier.getAsDouble(),
        // m_translationYSupplier.getAsDouble(),
        // m_rotationSupplier.getAsDouble()
        // )

    }

    
    private double square(double x, double divisor) {
        x = x / divisor;
        if (x < 0)
            return -(x * x);
        else
            return (x * x);
    }

    @Override
    public void end(boolean interrupted) {
        m_drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}
