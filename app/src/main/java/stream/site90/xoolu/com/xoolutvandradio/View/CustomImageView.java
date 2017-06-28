package stream.site90.xoolu.com.xoolutvandradio.View;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CustomImageView extends com.flaviofaria.kenburnsview.KenBurnsView{
    public CustomImageView(Context context) {
        super(context);
    }
    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireHeight= View.MeasureSpec.getSize(widthMeasureSpec) * 2/3;
        int desireHeightSpeck= View.MeasureSpec.makeMeasureSpec(desireHeight, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desireHeightSpeck);
    }
}
