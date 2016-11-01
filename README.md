# BottomBar
可以给底部Tab设置selector背景资源的BottomBar，基于[Iiro Krankka](https://github.com/roughike)的[BottomBar](https://github.com/roughike/BottomBar)项目修改


之前看到`BottomBar`这个项目的效果非常炫酷，使用起来也非常方便，于是想把它用到自己的项目中来，但是由于我这个项目的UI设计是tab点击时两张icon不断切换，而BottomBar中是由color来渲染icon的颜色，所以只能自己改造这个库了。目前这个项目已经更新到2.0版本了，之前1.0版本时我也改造过一遍，2.0与1.0之间的更新改了好多地方，之前是可以通过menu.xml和代码两种方式来配置BottomBarTab，现在只能通过tab.xml这种方式了，所以在2.0的基础上，我又重新改了一次。让它既能使用xml来配置，又可以通过代码来添加BottomBarTab。

## 效果图
<img src="http://offfjcibp.bkt.clouddn.com/Screenshot_2016-10-27-16-57-46-734_com.wxx.bottombar.png" width="30%" />
<img src="http://offfjcibp.bkt.clouddn.com/Screenshot_2016-10-28-14-12-05-323_com.wxx.bottombar.png" width="30%" />

## Usage
使用xml配置方式就不写了，可参考原项目工程。这里只介绍使用代码的方式。

```java
BottomBar mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

List<BottomBarTab> bottomBarItems = new ArrayList<>();

BottomBarTab homeTab = new BottomBarTab(this, R.drawable.selector_rb_home, R.string.main_home);
bottomBarItems.add(homeTab);

BottomBarTab chatTab = new BottomBarTab(this, R.drawable.selector_rb_chat, R.string.main_chat);
bottomBarItems.add(chatTab);

BottomBarTab squareTab = new BottomBarTab(this, R.drawable.selector_rb_square, R.string.main_square);
bottomBarItems.add(squareTab);

BottomBarTab discoverTab = new BottomBarTab(this, R.drawable.selector_rb_discover, R.string.main_discover);
bottomBarItems.add(discoverTab);

BottomBarTab meTab = new BottomBarTab(this, R.drawable.selector_rb_mine, R.string.main_mine);
bottomBarItems.add(meTab);

mBottomBar.setItems(bottomBarItems);
```

这里的setItems(List<BottomBarTab>)方式是我添加的，直接给BottomBar传递一个BottomBarTab的集合去初始化。

```java
/**************** update start *******************/
public void setItems(List<BottomBarTab> bottomBarItems) {
	if (bottomBarItems == null || bottomBarItems.size() <= 0) {
		throw new RuntimeException("No items specified for the BottomBar!");
	}
	//给BottomBarTab设置属性参数
	for (int i = 0; i < bottomBarItems.size(); i++) {
		BottomBarTab bottomBarTab = bottomBarItems.get(i);
		bottomBarTab.setActiveAlpha(activeTabAlpha);
		bottomBarTab.setInActiveAlpha(inActiveTabAlpha);
		bottomBarTab.setActiveColor(activeTabColor);
		bottomBarTab.setInActiveColor(inActiveTabColor);
		bottomBarTab.setBarColorWhenSelected(defaultBackgroundColor);
		//这一步很关键
		bottomBarTab.setIndexInContainer(i);
	}
	updateItems(bottomBarItems);
}
/**************** update end *******************/
```

这种方式是我们手动去setItems，但如果是使用xml的话，是在BottomBar的init方式中去调用的setItems(@XmlRes int xmlRes)方法，所以这里需要判断是否使用的是xml方式。

```java
private void init(Context context, AttributeSet attrs) {
	populateAttributes(context, attrs);
	initializeViews();
	determineInitialBackgroundColor();

	/**************** update start *******************/
	//此处改成根据有没有加载到tabXmlResource来判断调用哪种方式的setItems来设置tab
	if (tabXmlResource != 0) {
		setItems(tabXmlResource);
	}
	/**************** update end *******************/
}
```

另外，BottomBarTab新加了一个构造方法，将selector资源和title的string资源传递进来。

```java
/**************** update start *******************/
private int bgResource;
private int titleResource;

public int getBgResource() {
	return bgResource;
}

public void setBgResource(int bgResource) {
	this.bgResource = bgResource;
}

public int getTitleResource() {
	return titleResource;
}

public void setTitleResource(int titleResource) {
	this.titleResource = titleResource;
}

public BottomBarTab(Context context, @DrawableRes int bgResource, @NonNull int titleResource) {
	super(context);

	sixDps = MiscUtils.dpToPixel(context, 6);
	eightDps = MiscUtils.dpToPixel(context, 8);
	sixteenDps = MiscUtils.dpToPixel(context, 16);

	this.bgResource = bgResource;
	this.titleResource = titleResource;
}
/**************** update end *******************/
```

关于点击Tab切换时，图标和文字的动画效果，文字是做了一个缩放动画，图标是一个往上的平移动画，这里需要解释一个问题：原项目中BottomBarTab设置icon是给`android.support.v7.widget.AppCompatImageView`设置src属性，而selector资源只能设置给他的`background`。如果是设置src就可以给icon图标设置padding内边距，而原项目的图标的平移动画也就是改变ImageView的icon与顶部之间paddingTop值，但是如果设置background就无法设置内边距了，这里为了使ImageView与顶部保持一定的间距，我只能手动给ImageView设置一个marginTop，放弃icon的平移动画了。

```java
/**************** update start *******************/
//如果是设置背景资源的话，由于不是src，图片和控件之间没法设置padding，所以点击选中时图片往上的那个动画会失效
//这里需要给iconView设置一个与顶部之间的marginTop

//之前是给iconView设置icon图标，setImageResource，现在我们要用setBackgroundResource，设置背景资源
if (bgResource != 0) {
	iconView.setBackgroundResource(bgResource);

	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
	params.setMargins(0, MiscUtils.dpToPixel(getContext(), 8), 0, 0);
	iconView.setLayoutParams(params);
} else {
	iconView.setImageResource(iconResId);
}

//文字我也选择从string.xml中加载
if (type != Type.TABLET) {
	titleView = (TextView) findViewById(R.id.bb_bottom_bar_title);
	if (titleResource != 0) {
		titleView.setText(getContext().getString(titleResource));
	} else {
		titleView.setText(title);
	}

}
/**************** update end *******************/
```

另外，在BottomBarTab的select()和deselect()中，需要分别调用`setSelected(true);`和`setSelected(false);`，还有tab的点击监听处理：

```java
private void updateSelectedTab(int newPosition) {

	if (tabXmlResource != 0) { //使用xml配置tabs

		int newTabId = getTabAtPosition(newPosition).getId();

		if (newPosition != currentTabPosition) {
			if (onTabSelectListener != null) {
				onTabSelectListener.onTabSelected(newTabId);
			}
		} else if (onTabReselectListener != null && !ignoreTabReselectionListener) {
			onTabReselectListener.onTabReSelected(newTabId);
		}

	} else { //手动创建tabs

		if (newPosition != currentTabPosition) {
			if (onTabSelectListener != null) {
				onTabSelectListener.onBarSelected(newPosition);
			}
		} else if (onTabReselectListener != null && !ignoreTabReselectionListener) {
			onTabReselectListener.onTabReSelected(newPosition);
		}

	}

	currentTabPosition = newPosition;

	if (ignoreTabReselectionListener) {
		ignoreTabReselectionListener = false;
	}
}
```

使用xml配置tabs是根据tab在xml中的id来判断点击的是哪个tab，而代码创建tabs只能根据位置索引position来判断了，所以刚才初始化BottomBarTab时设置了一句`bottomBarTab.setIndexInContainer(i);`，就是初始化每个tab的位置索引，如果不写这句，后面的点击是无效的。为了区别`OnTabSelectListener`中的`void onTabSelected(@IdRes int tabId);`方法，我新加了一个`void onBarSelected(int position);`。

```java
mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {

	@Override public void onTabSelected(@IdRes int tabId) {
		switch (tabId) {
			case R.id.tab_home:
				switchToFragment(BOTTOM_ITEM_TITLE_HOME_INDEX);
				break;
			case R.id.tab_chat:
				switchToFragment(BOTTOM_ITEM_TITLE_CHAT_INDEX);
				break;
			case R.id.tab_square:
				switchToFragment(BOTTOM_ITEM_TITLE_SQUARE_INDEX);
				break;
			case R.id.tab_discover:
				switchToFragment(BOTTOM_ITEM_TITLE_DISCOVER_INDEX);
				break;
			case R.id.tab_me:
				switchToFragment(BOTTOM_ITEM_TITLE_ME_INDEX);
				break;
		}
	}

	@Override
	public void onBarSelected(int position) {
		switch (position) {
			case BOTTOM_ITEM_TITLE_HOME_INDEX:
				switchToFragment(BOTTOM_ITEM_TITLE_HOME_INDEX);
				break;
			case BOTTOM_ITEM_TITLE_CHAT_INDEX:
				switchToFragment(BOTTOM_ITEM_TITLE_CHAT_INDEX);
				break;
			case BOTTOM_ITEM_TITLE_SQUARE_INDEX:
				switchToFragment(BOTTOM_ITEM_TITLE_SQUARE_INDEX);
				break;
			case BOTTOM_ITEM_TITLE_DISCOVER_INDEX:
				switchToFragment(BOTTOM_ITEM_TITLE_DISCOVER_INDEX);
				break;
			case BOTTOM_ITEM_TITLE_ME_INDEX:
				switchToFragment(BOTTOM_ITEM_TITLE_ME_INDEX);
				break;
		}
	}
});
```

这里我只处理了点击一次的监听，`OnTabReselectListener`这个接口我没有处理，因为没有这个需求，所以不赘述了。


## LICENSE
```
Copyright (c) 2016 shenhuniurou

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
