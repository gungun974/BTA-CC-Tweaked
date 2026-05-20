/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.apis;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IWorkMonitor;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.core.computer.IComputerEnvironment;
import dan200.computercraft.core.filesystem.FileSystem;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.tracking.TrackingField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAPIEnvironment {
    String TIMER_EVENT = "timer";

    int getComputerID();

    @NotNull
    IComputerEnvironment getComputerEnvironment();

    @NotNull
    IWorkMonitor getMainThreadMonitor();

    @NotNull
    Terminal getTerminal();

    FileSystem getFileSystem();

    void shutdown();

    void reboot();

    void queueEvent(String event, Object... args);

    void setOutput(ComputerSide side, int output);

    int getOutput(ComputerSide side);

    int getInput(ComputerSide side);

    void setBundledOutput(ComputerSide side, int output);

    int getBundledOutput(ComputerSide side);

    int getBundledInput(ComputerSide side);

    void setPeripheralChangeListener(@Nullable IPeripheralChangeListener listener);

    @Nullable
    IPeripheral getPeripheral(ComputerSide side);

    String getLabel();

    void setLabel(@Nullable String label);

    int startTimer(long ticks);

    void cancelTimer(int id);

    void addTrackingChange(@NotNull TrackingField field, long change);

    default void addTrackingChange(@NotNull TrackingField field) {
        addTrackingChange(field, 1);
    }

    @FunctionalInterface
    interface IPeripheralChangeListener {
        void onPeripheralChanged(ComputerSide side, @Nullable IPeripheral newPeripheral);
    }
}
