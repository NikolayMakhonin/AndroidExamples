package nikolay_makhonin.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;

import java.util.concurrent.Semaphore;

import nikolay_makhonin.utils.contracts.RefParam;
import nikolay_makhonin.utils.contracts.delegates.Func;
import nikolay_makhonin.utils.logger.Log;
import nikolay_makhonin.utils.threads.ThreadUtils;

public class DialogWaiter {

    private Func<AlertDialog> _dialogConstructor;

    public DialogWaiter(Func<AlertDialog> constructor) {
        _dialogConstructor = constructor;
    }

    public void showAndWait() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);

        semaphore.acquire();

        final RefParam<Looper> looper = new RefParam<>(null);

        Thread thread = ThreadUtils.newThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();

                    looper.value = Looper.myLooper();

                    final AlertDialog dialog = _dialogConstructor.invoke();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            semaphore.release();
                        }
                    });

                    dialog.show();

                    Looper.loop();
                } catch (Throwable e) {
                    Log.e("DialogWaiter", null, e);
                }
            }
        }, "DialogWaiter");
        try {
            thread.start();
            semaphore.acquire();
            thread.join(300);
        } finally {
            if (looper.value != null) {
                looper.value.quit();
            }
        }

        thread.join();
    }
}
