ScreenColorTest（屏幕颜色测试）

==============================================================
版本V1.0
日期：2016-12-01
==============================================================

Function（功能说明）:
==============================================================
1、全屏黑色测试
2、全屏白色测试
3、全屏红色测试
4、全屏绿色测试
5、全屏蓝色测试
6、全屏三等分显示红绿蓝三种颜色
7、全屏黑白方格交替显示
==============================================================

Technical Description（技术说明）：
=========================================================================================
一、全屏显示（隐藏状态栏和虚拟按键栏（如果有），并且点击屏幕也不会显示状态栏和虚拟按键栏（如果有），
				只有从屏幕底部向上滑或从屏幕顶部向下滑才显示状态栏和虚拟按键栏（如果有））：
				1、只需要在Activity的onCreate()方法中的super.onCreate();语句前添加如下语句即可：
				getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        
二、保持屏幕常亮，不锁屏：
				1、只需在Activity的onCreate()方法中的super.onCreate();语句前添加如下语句即可：
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                
三、画黑白方格：
				1、计算方格坐标：
								a. 计算方格的坐标采用逐行计算方法，方格的长度和高度采用固定值。计算方法如下：
									public void initRects() {
						        int width = getWidth();
						        int height = getHeight();
						        mRects = new ArrayList<RectItem>();
						        if (width != 0 && height != 0) {
						            for (int row = 0, i = 0; i <= height; i += mRectHeight, row++) {
						                for (int j = 0; j <= width; j += mRectWidth) {
						                    RectItem item = new RectItem(j, i, j + mRectWidth, i + mRectHeight, row);
						                    mRects.add(item);
						                }
						            }
						        }
						    }
					    
					    b. 计算每个方格使用的画笔。首先使用position记录方格所在行的位置，当方格处于新行时，将position重置为0.
					        通过方格所在的行计算第一个方格使用的画笔，通过position计算一行中每个方格使用的画笔。计算方法如下：
					        private Paint getPaint(int position, RectItem item) {
						        // 初始化第一行第一个矩形的画笔startPaint
						        Paint startPaint = mBlackPaint;
						        Paint secontPaint = mWhitePaint;
						        if (((item.row + 1) % 2) == 0) {
						            startPaint = mWhitePaint;
						            secontPaint = mBlackPaint;
						        }
						        // 在一行中交替变换画笔
						        Paint paint = startPaint;
						        if (((position + 1) % 2) == 0) {
						            paint = secontPaint;
						        }
						        return paint;
						    }
==================================================================================================

******************************************** V1.0 END ******************************************************
