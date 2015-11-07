<h3>AutoChangeIndicator</h3>

练习写的一个ViewPagerIndicator，其中指示器宽度可跟随设置的tab宽度改变而改变


![效果图](https://github.com/wa123fei/AutoChangeIndicator/blob/master/indicator.gif)
----------


<h3>功能</h3>

 - 通过setTitles方法插入标题数据
 - 支持一些自定义属性


----------


<h3>声明</h3>

在布局文件中声明

```
<com.example.lx.myproject.view.AutoChangeIndicator
        android:id="@+id/msi_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        indicator:indicator_color="#ffffff"
        indicator:indicator_height="2dp"
        indicator:title_backColor="#00ffffff"
        indicator:title_textColor="#dde2e2e2"
        indicator:title_margin="20dp"
        indicator:title_textHColor="#f3fcfffe"
        indicator:title_textSize="14sp">
```
<h4>支持的属性:</h4>

 - title_margin 标题左右边距
 - indicator_color 指示器颜色
 - indicator_height 指示器高度
 - title_textSize 标题字体大小
 - title_textColor 标题字体颜色
 - title_textHColor 标题字体高亮颜色
 - title_backColor 标题背景
 - title_padding 标题内边距

<h3>使用</h3>

```
autoChangeIndicator = (AutoChangeIndicator) findViewById(R.id.indicator);
autoChangeIndicator.setTitles(titles);
autoChangeIndicator.setViewPager(mViewPager);
```

