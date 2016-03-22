package nikolay_makhonin.utils.logger;

import nikolay_makhonin.utils.events.EventArgs;

public class LogEventArgs extends EventArgs {
    public final LogEventInfo logInfo;

    public LogEventArgs(LogEventInfo logInfo) {
        this.logInfo = logInfo;
    }
}
