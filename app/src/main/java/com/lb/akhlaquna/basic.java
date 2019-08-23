package com.lb.akhlaquna;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by richard.navin on 8/22/2019.
 */

class basic {

    // setTimeout, setInterval
    public interface TaskHandle {
        void invalidate();
    }

    public static TaskHandle setTimeout(final Runnable r, long delay) {
        final Handler h = new Handler();
        h.postDelayed(r, delay);
        return new TaskHandle() {
            @Override
            public void invalidate() {
                h.removeCallbacks(r);
            }
        };
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
