package nikolay_makhonin.utils.logger;

public class LogConsole implements ILog {
    @Override
    public int v(final String tag, final String msg) {
        log("Log.v", tag, msg);
        return 0;
    }

    @Override
    public int v(final String tag, final String msg, final Throwable tr) {
        log("Log.v", tag, msg, tr);
        return 0;
    }

    @Override
    public int d(final String tag, final String msg) {
        log("Log.d", tag, msg);
        return 0;
    }

    @Override
    public int d(final String tag, final String msg, final Throwable tr) {
        log("Log.d", tag, msg, tr);
        return 0;
    }

    @Override
    public int i(final String tag, final String msg) {
        log("Log.i", tag, msg);
        return 0;
    }

    @Override
    public int i(final String tag, final String msg, final Throwable tr) {
        log("Log.i", tag, msg, tr);
        return 0;
    }

    @Override
    public int w(final String tag, final String msg) {
        log("Log.w", tag, msg);
        return 0;
    }

    @Override
    public int w(final String tag, final String msg, final Throwable tr) {
        log("Log.w", tag, msg, tr);
        return 0;
    }

    @Override
    public int w(final String tag, final Throwable tr) {
        log("Log.w", tag, tr);
        return 0;
    }

    @Override
    public int e(final String tag, final String msg) {
        log("Log.e", tag, msg);
        return 0;
    }

    @Override
    public int e(final String tag, final String msg, final Throwable tr) {
        log("Log.e", tag, msg, tr);
        return 0;
    }

    @Override
    public int wtf(final String tag, final String msg) {
        log("Log.wtf", tag, msg);
        return 0;
    }

    @Override
    public int wtf(final String tag, final Throwable tr) {
        log("Log.wtf", tag, tr);
        return 0;
    }

    @Override
    public int wtf(final String tag, final String msg, final Throwable tr) {
        log("Log.wtf", tag, msg, tr);
        return 0;
    }

    @Override
    public String getStackTraceString(final Throwable tr) {
        log("Log.getStackTraceString", tr);
        return "";
    }

    @Override
    public int println(final int priority, final String tag, final String msg) {
        log("Log.println", priority, tag, msg);
        return 0;
    }

    private void log(final String funcName, final Object... objects) {
        System.err.print("------------ ");
        System.err.print(funcName);
        System.err.println(" ------------");
        for (final Object object : objects) {
            if (object instanceof Throwable) {
                ((Throwable) object).printStackTrace(System.err);
            } else {
                System.err.println(object);
            }
        }
    }
}
