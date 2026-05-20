/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.apis.http.options;

import org.jetbrains.annotations.NotNull;

/**
 * Options about a specific domain.
 */
public final class Options {
    @NotNull
    public final Action action;
    public final long maxUpload;
    public final long maxDownload;
    public final int timeout;
    public final int websocketMessage;

    Options(@NotNull Action action, long maxUpload, long maxDownload, int timeout, int websocketMessage) {
        this.action = action;
        this.maxUpload = maxUpload;
        this.maxDownload = maxDownload;
        this.timeout = timeout;
        this.websocketMessage = websocketMessage;
    }
}
