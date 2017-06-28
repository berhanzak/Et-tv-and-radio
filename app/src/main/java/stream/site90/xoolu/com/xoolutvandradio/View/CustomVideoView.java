package stream.site90.xoolu.com.xoolutvandradio.View;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
public class CustomVideoView extends com.devbrackets.android.exomedia.ui.widget.VideoView{
    public CustomVideoView(Context context) {
        super(context);
    }
    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireHeight= View.MeasureSpec.getSize(widthMeasureSpec) * 2/3;
        int desireHeightSpeck= View.MeasureSpec.makeMeasureSpec(desireHeight, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desireHeightSpeck);
    }
}
