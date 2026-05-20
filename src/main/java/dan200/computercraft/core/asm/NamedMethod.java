/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.asm;

import org.jetbrains.annotations.NotNull;

public final class NamedMethod<T> {
    private final String name;
    private final T method;
    private final boolean nonYielding;

    NamedMethod(String name, T method, boolean nonYielding) {
        this.name = name;
        this.method = method;
        this.nonYielding = nonYielding;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public T getMethod() {
        return method;
    }

    public boolean nonYielding() {
        return nonYielding;
    }
}
