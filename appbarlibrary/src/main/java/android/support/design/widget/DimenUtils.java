package android.support.design.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;

import com.gu.appbarlibray.R;

/**
 * Created by gu on 2017/5/6.
 */

public class DimenUtils {
    public static int getActionBarHeight(Context context) {
        //        final TypedValue tv = new TypedValue();
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        //            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        //                return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        //            }
        //        } else {
        //            // 使用android.support.v7.appcompat包做actionbar兼容的情况  
        //            if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
        //                return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        //            }
        //        }

        return context.getResources().getDimensionPixelOffset(R.dimen.top_action_bar_height);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
