package com.example.libindicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libindicator.R;
import com.example.libindicator.util.DensityUtils;

import java.util.List;

/**
 * Created by runing on 2015-10-23.
 * 基于HorizontalScrollView的自动改变宽度的ViewPager指示器
 */
public class AutoChangeIndicator extends HorizontalScrollView {

    /**
     * 内容View
     */
    private LinearLayout contentView;
    /**
     * 内容布局内部子宽
     */
    private TitleViewInfo[] contentChildViews;
    /**
     * 标题左右边距
     */
    private int titleMargin = 15;
    /**
     * 标题字体大小
     */
    private int titleTextSize = 20;
    /**
     * 标题字体颜色
     */
    private int titleTextColor = Color.BLACK;
    /**
     * 标题高亮颜色
     */
    private int titleTextHColor = Color.WHITE;
    /**
     * 标题内边距
     */
    private int titlePadding = 8;
    /**
     * 标题背景
     */
    private int titleBackColor = Color.parseColor("#00FFFFFF");
    /**
     * 标题数量
     */
    private int titleCount = 0;
    /**
     * 指示器起始X
     */
    private int indicatorStartX = 0;
    /**
     * 指示器起始y
     */
    private int indicatorStartY = 0;
    /**
     * 指示器高
     */
    private int indicatorHeight = 4;
    /**
     * 指示器宽
     */
    private int indicatorWidth = 0;
    /**
     * 指示器颜色
     */
    private int indicatorColor = Color.parseColor("#DAEB7BFA");
    /**
     * 指示器图形
     */
    private final Rect indicator = new Rect();
    /**
     * 指示器画笔
     */
    private final Paint paint = new Paint();
    /**
     * 设置的ViewPager
     */
    private ViewPager mViewPager;
    /**
     * 需要滚动的距离
     */
    private int mScrollLength = 0;
    /**
     * 初始滚动距离
     */
    private int mScrollStartLength = 0;
    /**
     * 手指离开后的像素值
     */
    private int lastPixels = -1;
    /**
     * 向左滑动
     */
    private static final int ORIENTATION_LEFT = 0;
    /**
     * 向右滑动
     */
    private static final int ORIENTATION_RIGHT = 1;
    /**
     * 滑动方向
     */
    private int orientation = -1;
    /**
     * 标题内容
     */
    private List<String> titles;

    /**
     * 标题view信息
     */
    class TitleViewInfo {
        MarginLayoutParams params;
        int viewWidth;
    }

    /**
     * 内部的LinearLayout
     */
    class ContentView extends LinearLayout {
        public ContentView(Context context) {
            super(context);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            if (getChildCount() == 0) {
                return;
            }
            MarginLayoutParams contentParams = (MarginLayoutParams) getChildAt(0)
                    .getLayoutParams();
            mScrollStartLength = contentParams.leftMargin;

            int contentHeight = h;
            int childCount = getChildCount();

            contentChildViews = new TitleViewInfo[childCount];

            indicatorWidth = getChildAt(0).getMeasuredWidth();
            indicatorStartX = mScrollStartLength + getPaddingLeft();
            indicatorStartY = contentHeight + getPaddingTop();

            for (int i = 0; i < childCount; i++) {
                MarginLayoutParams params = (MarginLayoutParams) getChildAt(i)
                        .getLayoutParams();
                contentChildViews[i] = new TitleViewInfo();
                contentChildViews[i].params = params;
                contentChildViews[i].viewWidth = getChildAt(i).getMeasuredWidth();
            }
        }
    }

    public AutoChangeIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAllAttrs(context, attrs);
        initPaint();
        initContentView();
    }

    public AutoChangeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAllAttrs(context, attrs);
        initPaint();
        initContentView();
    }

    private void initContentView() {
        addView(contentView = generateContentView());
        setHorizontalFadingEdgeEnabled(false);
        setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAllAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoChangeIndicator);
        titleMargin = (int) array.getDimension(R.styleable.AutoChangeIndicator_title_margin, titleMargin);
        indicatorColor = array.getColor(R.styleable.AutoChangeIndicator_indicator_color, indicatorColor);
        indicatorHeight = (int) array.getDimension(R.styleable.AutoChangeIndicator_indicator_height, indicatorHeight);
        titleTextSize = (int) DensityUtils.px2sp(getContext(), array.getDimension(R.styleable.AutoChangeIndicator_title_textSize, titleTextSize));
        titleTextColor = array.getColor(R.styleable.AutoChangeIndicator_title_textColor, titleTextColor);
        titleTextHColor = array.getColor(R.styleable.AutoChangeIndicator_title_textHColor, titleTextHColor);
        titleBackColor = array.getColor(R.styleable.AutoChangeIndicator_title_backColor, titleBackColor);
        titlePadding = (int) array.getDimension(R.styleable.AutoChangeIndicator_title_padding, titlePadding);
        array.recycle();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(indicatorColor);
        paint.setAlpha(1000);
    }

    /**
     * 设置标题内容
     *
     * @param titles
     */
    public void setTitles(List<String> titles) {
        this.titles = titles;
        titleCount = titles.size();
        for (int i = 0; i < titleCount; i++) {
            final TextView titleView = generateTitleView();
            titleView.setText(titles.get(i));
            contentView.addView(titleView);
        }
        initTitles();
    }

    /**
     * 设置高亮
     *
     * @param position
     */
    private void lightTitle(int position) {
        for (int i = 0; i < titleCount; i++) {
            ((TextView) contentView.getChildAt(i)).setTextColor(titleTextColor);
        }
        ((TextView) contentView.getChildAt(position)).setTextColor(titleTextHColor);
    }

    /**
     * 初始化标题
     */
    private void initTitles() {
        lightTitle(0);
        int childCount = contentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int position = i;
            contentView.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lightTitle(position);
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(position, false);
                    }
                }
            });
        }
    }

    /**
     * 产生ContentView
     *
     * @return
     */
    private LinearLayout generateContentView() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ContentView ll = new ContentView(getContext());
        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        return ll;
    }

    /**
     * 产生TitleView
     *
     * @return
     */
    private TextView generateTitleView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(getContext());
        params.rightMargin = params.leftMargin = titleMargin;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(titlePadding, titlePadding, titlePadding, titlePadding);
        textView.setBackgroundColor(titleBackColor);
        textView.setTextColor(titleTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        return textView;
    }

    /**
     * 设置ViewPager
     *
     * @param mViewPager
     */
    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                //判断滑动方向
                if (lastPixels > positionOffsetPixels) {
                    orientation = ORIENTATION_LEFT;
                } else if (lastPixels < positionOffsetPixels) {
                    orientation = ORIENTATION_RIGHT;
                }
                lastPixels = positionOffsetPixels;
                myScroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                lightTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 绘制指示器
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(indicatorStartX + mScrollLength, indicatorStartY);
        indicator.set(0, 0, indicatorWidth, indicatorHeight);
        canvas.drawRect(indicator, paint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 处理滑动
     *
     * @param position 页面下标
     * @param offset   滑动程度
     */
    private void myScroll(int position, float offset) {
        int tmpScrollLength = 0;
        for (int i = 0; i < position; i++) {
            tmpScrollLength += contentChildViews[i].viewWidth + contentChildViews[i].params
                    .rightMargin * 2;
        }
        mScrollLength = (int) (tmpScrollLength + (contentChildViews[position].viewWidth +
                contentChildViews[position].params.rightMargin * 2) * offset);
        switch (orientation) {
            case ORIENTATION_LEFT:
                indicatorWidth += (int) ((contentChildViews[position].viewWidth - indicatorWidth)
                        * (1 - offset));
                break;
            case ORIENTATION_RIGHT:
                indicatorWidth += (int) ((contentChildViews[position + 1].viewWidth -
                        indicatorWidth) * offset);
                break;
        }

        if (position > 0) {
            tmpScrollLength -= contentChildViews[position - 1].viewWidth +
                    contentChildViews[position - 1].params.rightMargin * 2;
            scrollTo(tmpScrollLength + (int) ((contentChildViews[position - 1].viewWidth +
                    contentChildViews[position - 1].params.leftMargin * 2) * offset), 0);
        } else {
            scrollTo(0, 0);
        }
        invalidate();
    }

    /**
     * 重新设置宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + indicatorHeight);
    }

}
