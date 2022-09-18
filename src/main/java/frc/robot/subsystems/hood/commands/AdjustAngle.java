package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.hood.Hood;

import java.util.function.DoubleSupplier;

public class AdjustAngle extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final DoubleSupplier angle;
    private final Timer timer = new Timer();
    private double lastTime = 1;
    private double lastAngle = 1;
    private double currentAngle;
    private double lastVelocity = 1;
    private double currentVelocity = 1;
    private double maxVelocity = 1;
    private double maxAcceleration = 1;

    public AdjustAngle(DoubleSupplier angle) {
        this.angle = angle;
        addRequirements(hood);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        hood.setAngle(angle.getAsDouble());
        double currentTime = timer.get();

        lastAngle = currentAngle;
        currentAngle = hood.getAngle();

        lastVelocity = currentVelocity;
        currentVelocity = (currentAngle - lastAngle) / (currentTime - lastTime);

        maxAcceleration = Math.max(maxAcceleration, (currentVelocity - lastVelocity) / (currentTime - lastTime));
        maxVelocity = Math.max(currentVelocity, maxVelocity);
        System.out.println("vel : " + maxVelocity);
        System.out.println("accel : " + maxAcceleration);

        lastTime = currentTime;
    }

    @Override
    public void end(boolean interrupted) {
        hood.stop();
    }

    @Override
    public boolean isFinished() {
        return hood.atSetpoint(Constants.Hood.ALLOWABLE_ERROR);
    }
}
