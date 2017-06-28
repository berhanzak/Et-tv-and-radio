package stream.site90.xoolu.com.xoolutvandradio.View;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
public class CustomWebView extends im.delight.android.webview.AdvancedWebView{
    public CustomWebView(Context context) {
        super(context);
    }
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireHeight= View.MeasureSpec.getSize(widthMeasureSpec) * 2/3;
        int desireHeightSpeck= View.MeasureSpec.makeMeasureSpec(desireHeight, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desireHeightSpeck);
    }
}
