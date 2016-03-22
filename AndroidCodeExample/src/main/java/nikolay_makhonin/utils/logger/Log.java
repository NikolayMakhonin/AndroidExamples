package nikolay_makhonin.utils.logger;


import nikolay_makhonin.androidcodeexample.BuildConfig;
import nikolay_makhonin.utils.events.EventHandler;
import nikolay_makhonin.utils.events.IEventHandler;
import nikolay_makhonin.utils.threads.ThreadUtils;

public class Log {
    public static  ILog    logger;
    public static  String  tagPrefix;
    private static boolean _debugMode;

    static {
        _debugMode = BuildConfig.DEBUG;
        tagPrefix = "MyApp.";
        //logger = new LogConsole();
        try {
            ClassLoader.getSystemClassLoader().loadClass("android.util.Log");
            logger = new LogAndroid();
        } catch (final ClassNotFoundException e) {
            logger = new LogConsole();
        }
    }

    public static int v(final String tag, final String msg) {
        if (_debugMode) {
            logger.v(tagPrefix + tag, msg);
            OnLog(LogPriority.Verbose, tag, msg, null);
        }
        return 0;
    }

    public static int v(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.v(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            OnLog(LogPriority.Verbose, tag, msg, tr);
        }
        return 0;
    }

    public static int d(final String tag, final String msg) {
        if (_debugMode) {
            logger.d(tagPrefix + tag, msg);
            OnLog(LogPriority.Debug, tag, msg, null);
        }
        return 0;
    }

    public static int d(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.d(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            OnLog(LogPriority.Debug, tag, msg, tr);
        }
        return 0;
    }

    public static int i(final String tag, final String msg) {
        if (_debugMode) {
            logger.i(tagPrefix + tag, msg);
            OnLog(LogPriority.Info, tag, msg, null);
        }
        return 0;
    }

    public static int i(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.i(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            OnLog(LogPriority.Info, tag, msg, tr);
        }
        return 0;
    }

    public static int w(final String tag, final String msg) {
        logger.w(tagPrefix + tag, msg);
        OnLog(LogPriority.Warn, tag, msg, null);
        return 0;
    }

    public static int w(final String tag, final String msg, final Throwable tr) {
        logger.w(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        OnLog(LogPriority.Warn, tag, msg, tr);
        return 0;
    }

    public static int w(final String tag, final Throwable tr) {
        logger.w(tagPrefix + tag, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        OnLog(LogPriority.Error, tag, null, tr);
        return 0;
    }

    public static int e(final String tag, final String msg) {
        logger.e(tagPrefix + tag, msg);
        OnLog(LogPriority.Error, tag, msg, null);
        return 0;
    }

    public static int e(final String tag, final String msg, final Throwable tr) {
        logger.e(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        OnLog(LogPriority.Error, tag, msg, tr);
        return 0;
    }

    public static int wtf(final String tag, final String msg) {
        logger.wtf(tagPrefix + tag, msg);
        OnLog(LogPriority.Assert, tag, msg, null);
        return 0;
    }

    public static int wtf(final String tag, final Throwable tr) {
        logger.wtf(tagPrefix + tag, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        OnLog(LogPriority.Assert, tag, null, null);
        return 0;
    }

    public static int wtf(final String tag, final String msg, final Throwable tr) {
        logger.wtf(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        OnLog(LogPriority.Assert, tag, msg, tr);
        return 0;
    }

    public static String getStackTraceString(final Throwable tr) {
        logger.getStackTraceString(tr);
        return "";
    }

    public static int println(final int priority, final String tag, final String msg) {
        logger.println(priority, tagPrefix + tag, msg);
        OnLog(priority, tag, msg, null);
        return 0;
    }

    public static int e(final LogEventInfo logInfo) {
        logger.e(logInfo.tag, logInfo.message, logInfo.exception);
        if (logInfo.exception != null) {
            logInfo.exception.printStackTrace();
        }
        OnLog(logInfo);
        return 0;
    }

    //region Log event

    public static void OnLog(LogEventInfo logInfo) {
        if (_logEvent != null) {
            _logEvent.invoke(null, new LogEventArgs(logInfo));
        }
    }

    public static void OnLog(int priority, String tag, String msg, Throwable tr) {
        if (_logEvent != null) {
            _logEvent.invoke(null, new LogEventArgs(new LogEventInfo(priority, tagPrefix + tag, msg, tr, false)));
        }
    }

    private static EventHandler<LogEventArgs> _logEvent;

    public static IEventHandler<LogEventArgs> LogEvent() {
        return _logEvent != null ? _logEvent : (_logEvent = new EventHandler<LogEventArgs>());
    }

    //endregion

    public static final Thread.UncaughtExceptionHandler uncaughtExceptionHandler
        = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable e)
        {
            handleUncaughtException(thread, e);
        }
    };

    private static void handleUncaughtException(Thread thread, Throwable e)
    {
        String threadStackTrace = null;
        try {
            threadStackTrace = ThreadUtils.stackTraceToString(thread.getStackTrace());
        } catch (Exception e2) {

        }

        LogEventInfo logInfo = new LogEventInfo(LogPriority.Error, tagPrefix + "UncaughtException",
            "Thread StackTrace: " + threadStackTrace, e, _logEvent != null && ThreadUtils.isMainThread());

        Log.e(logInfo);
    }
}

