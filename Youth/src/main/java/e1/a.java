package e1;

import android.content.Context;

/**
 * Date：2024/6/20
 * Describe:
 */
public class a {
    //注意:第2个参数传字符串::字符串包含"ch"隐藏图标,包含"ju"恢复隐藏.包含"in"外弹(外弹在子线程调用).(保证i参数不容易关联)

    public static native int a(Context context, String i);

}
