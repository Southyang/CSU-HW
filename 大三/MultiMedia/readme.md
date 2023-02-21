## 先来看实验要求
**设计实现一个基于Win32的应用程序，包括如下功能（满分100分）：**
- 能够通过文件选择对话框选定两个视频文件。如果你使用OpenCV则可以选择常规的mp4或avi格式视频；如果你不使用OpenCV则建议载入yuv视频。我们在第十二周的实验代码压缩包“YUV视频播放的参考代码和YUV视频文件.zip”中包含了两个yuv视频文件，可供你使用。（占10分）
- 能通过菜单选择任意一个视频进行播放（占10分），具有播放（占10分）、暂停（占10分）、停止功能（占10分）。
- 能通过菜单选择画中画效果，即在播放某一个视频时，同时把另外一个视频缩小至前一个视频画面的右上角一同播放（占30分，其中缩小占10分、右上角占10分、一同播放占10分）。在画中画播放过程中，任意视频如果播放到结尾，该视频就从头重播，周而复始（占20分，其中若两个视频都能分别周而复始播放给20分，若只有一个可以则给10分）。
- 如果实现了额外的视频效果，则最多可以奖励15分。可以选择的视频效果：
	- 1.可以用鼠标拖拽的方式改变画中画的小视频的位置（5分）；
	- 2.可以用鼠标在视频画面上绘制线条（5分）
	- 3.可以将画中画的小视频改为边缘图显示（即显示其画面的边缘图而不是原图）（5分）

## 再来看给出的代码模板
- yuv格式读取已实现内容
	- 播放视频fireman.yuv（路径还需要自己改成绝对路径）
- mp4格式读取已实现内容
	- 视频文件选择
	- 视频文件播放暂停停止
	- 视频文件边缘图显示

很明显，mp4格式读取，也就是OpenCV的模板更全🐶
那我肯定选**OpenCV**了！

**[实验代码在这里](https://github.com/Southyang/CSU-HW/tree/master/%E5%A4%A7%E4%B8%89/MultiMedia)**
**[实验视频传到B站啦](https://www.bilibili.com/video/BV1wY4y1r7St/)**

## 正式开始进行实验设计
### 1. 环境配置
选OpenCV唯一的问题就是要配置环境
详细配置见我的另一篇博客[OpenCV环境配置](https://southyang.cn/2022/05/02/%e5%9c%a8vs2019%e4%b8%ad%e9%85%8d%e7%bd%aeopencv/)

### 2. 选定两个视频文件并进行播放、暂停、停止
只需要模仿代码模板中的文件选择、状态操作即可
```cpp
// 参数定义
cv::Mat img1;                                   // 视频1帧
cv::Mat img2;                                   // 视频2帧
WCHAR FileNameOfVideo1[1024];                   // 视频1的文件路径和文件名
WCHAR FileNameOfVideo2[1024];                   // 视频2的文件路径和文件名
cv::VideoCapture VidCap1;                       // 视频1的读取器
cv::VideoCapture VidCap2;                       // 视频2的读取器
```
```cpp
// 选择文件
case IDM_OPEN_VID1:                
    result = OpenVideoFile(hWnd, &fn1);
    if (result)
    {
        //img1 = cv::imread(WCHAR2String(fn1));
        bool opened = VidCap1.open(WCHAR2String(fn1));
        frameNum1 = VidCap1.get(cv::CAP_PROP_FRAME_COUNT); // 获取总帧数
        if (opened)
        {
            VidCap1 >> img1; //获取第一帧图像并显示

            //激发WM_PAINT时间，让窗口重绘
            InvalidateRect(hWnd, NULL, false);
        }
        else
        {
            MessageBox(
                hWnd,
                L"视频未能打开",
                L"错误提示",
                MB_OK
            );
        }
    }
    break;
case IDM_OPEN_VID2:
    result = OpenVideoFile(hWnd, &fn2);
    if (result)
    {
        bool opened = VidCap2.open(WCHAR2String(fn2));
        frameNum2 = VidCap2.get(cv::CAP_PROP_FRAME_COUNT); // 获取总帧数
        if (opened)
        {
            VidCap2 >> img2; //获取第二帧图像并显示

            //激发WM_PAINT时间，让窗口重绘
            InvalidateRect(hWnd, NULL, false);
        }
        else
        {
            MessageBox(
                hWnd,
                L"视频未能打开",
                L"错误提示",
                MB_OK
            );
        }
    }
    bre
```
```cpp
// 播放状态
case IDM_PLAY_VID:
    playState1 = PlayState::playing;
    break;
case IDM_PAUSE_VID:
    playState1 = PlayState::paused;
    break;
case IDM_STOP_VID:
    playState1 = PlayState::stopped;
    VidCap1.set(cv::VideoCaptureProperties::CAP_PROP_POS_FRAMES, 0);                
    break;
case IDM_PLAY_VID2:
    playState2 = PlayState::playing;
    break;
case IDM_PAUSE_VID2:
    playState2 = PlayState::paused;
    break;
case IDM_STOP_VID2:
    playState2 = PlayState::stopped;
    VidCap2.set(cv::VideoCaptureProperties::CAP_PROP_POS_FRAMES, 0);
    bre
```
```cpp
// 计时器发布消息
case WM_TIMER: // 当计时器过期时，发布到正在安装的线程的消息队列。 消息由 GetMessage 或 PeekMessage 函数发布。
    if (VidCap1.isOpened() && playState1 == PlayState::playing)
    {
        VidCap1 >> img1;
        if (++frameNow1 >= frameNum1 - 1) {
            frameNow1 = 0;
            VidCap1.set(cv::VideoCaptureProperties::CAP_PROP_POS_FRAMES, 0);
        }
        if (img1.empty() == false)
        {
            if (vidEffect1 == VideoEffect::edge)
            {                    
                cv::Mat edgeY, edgeX;
                cv::Sobel(img1, edgeY, CV_8U, 1, 0);
                cv::Sobel(img1, edgeX, CV_8U, 0, 1);
                img1 = edgeX + edgeY;
            }

            InvalidateRect(hWnd, NULL, false);
        }
    }
    if (VidCap2.isOpened() && playState2 == PlayState::playing)
    {
        VidCap2 >> img2;
        if (++frameNow2 >= frameNum2 - 1) {
            frameNow2 = 0;
            VidCap2.set(cv::VideoCaptureProperties::CAP_PROP_POS_FRAMES, 0);
        }
        if (img2.empty() == false)
        {
            if (vidEffect2 == VideoEffect::edge)
            {
                cv::Mat edgeY, edgeX;
                cv::Sobel(img2, edgeY, CV_8U, 1, 0);
                cv::Sobel(img2, edgeX, CV_8U, 0, 1);
                img2 = edgeX + edgeY;
            }

            InvalidateRect(hWnd, NULL, false);
        }
    }
    break;
```
```cpp
// 绘制
int wWidth = rect.right - rect.left;
int wHeight = rect.bottom - rect.top;
int width1 = wWidth / 2;
int x1 = 0;

if (img1.cols != 0) {
    PlayVideo(img1, hdc1, hWnd, x1, 0, width1, width1 * img1.rows / img1.cols);
}

if (img2.cols != 0) {
    width2 = wWidth / 2;
    x2 = width1;
    height2 = width2 * img2.rows / img2.cols;
    y2 = 0;
    PlayVideo(img2, hdc1, hWnd, x2, y2, width2, height2);
}
```
### 3. 实现画中画模式
画中画就是改变两个视频的长宽和左上角位置
例如，普通模式是两个视频平分程序窗口的宽，在画中画模式下，主视频占据主体，将小视频缩小到主视频左上角
即将主视频的高设为窗口高度，宽度根据视频帧计算；
将小视频的高设为窗口的2/5，宽度根据视频帧计算；
```cpp
// 设定参数和状态
// 画中画参数
int width2, height2;                            // 视频文件2的长宽
int x2, y2;                                     // 视频文件2的位置
HDC hdc2;                                       // 视频文件2的hdc

enum PiPState {
    noPip, pip                                     
};
PiPState pipState = PiPState::noPip;             // 画中画状态
```
```cpp
// 修改状态
case IDM_INSERT_VID:
    if (pipState == PiPState::pip) {
        pipState = PiPState::noPip;
    }
    else {
        pipState = PiPState::pip;
    }
    break;
```
```cpp
// 改变长宽和坐标
if (pipState == PiPState::pip) {
    if (img1.cols != 0 && img2.cols != 0) {
        int wWidth = rect.right - rect.left;
        int wHeight = rect.bottom - rect.top;
        int width1 = img1.cols * wHeight / img1.rows;
        int x1 = (wWidth - width1) / 2;
        PlayVideo(img1, hdc1, hWnd, x1, 0, width1, wHeight);
        width2 = img2.cols * wHeight / img2.rows / 2.5;

        if (dragState == DragState::noDrag) {
            x2 = wWidth - x1 - width2;
            y2 = 0;
            height2 = wHeight / 2.5;
        }
        else {
            if (isDrag) {
                x2 += nowX - lastX;
                y2 += nowY - lastY;
                lastX = nowX;
                lastY = nowY;
            }
        }

        PlayVideo(img2, hdc1, hWnd, x2, y2, width2, height2);
    }
}
```
### 4. 循环反复播放
循环播放的原理是，当检测到当前视频帧已经全部播放完之后，从头开始
当前的状态是，视频播放到了最后就会一直停留在最后一帧不再播放；并且画中画模式下，较短的视频停止后会将另一个也停止
那么我们就要统计每一个视频的帧数并进行记录，如果当前是最后一帧，那么就从头播放；否则继续播放
```cpp
// 设定参数
int frameNum1;                                  // 视频1帧数
int frameNum2;                                  // 视频2帧数
int frameNow1 = 0;                              // 视频1当前帧数
int frameNow2 = 0;                              // 视频2当前帧数
```
```cpp
// 判断帧数，两个视频操作相同，只放一个了
if (++frameNow1 >= frameNum1 - 1) {
    frameNow1 = 0;
    VidCap1.set(cv::VideoCaptureProperties::CAP_PROP_POS_FRAMES, 0);
}
```
### 5. 边缘图显示
代码模板里都有了
```cpp
// 设定状态
enum VideoEffect
{
    noEdge, edge
};
VideoEffect vidEffect1 = VideoEffect::noEdge;    // 视频1画面状态
VideoEffect vidEffect2 = VideoEffect::noEdge;    // 视频2画面状态
```
```cpp
// 修改状态
case IDM_EDGE_EFFECT:
    if (vidEffect1 == VideoEffect::edge) {
        vidEffect1 = VideoEffect::noEdge;
    }
    else {
        vidEffect1 = VideoEffect::edge;
    }
    break;
case IDM_EDGE_EFFECT2:
    if (vidEffect2 == VideoEffect::edge) {
        vidEffect2 = VideoEffect::noEdge;
    }
    else {
        vidEffect2 = VideoEffect::edge;
    }
    break;
```
```cpp
// 改变显示状态
if (vidEffect1 == VideoEffect::edge)
{
    cv::Mat edgeY, edgeX;
    cv::Sobel(img1, edgeY, CV_8U, 1, 0);
    cv::Sobel(img1, edgeX, CV_8U, 0, 1);
    img1 = edgeX + edgeY;
}

InvalidateRect(hWnd, NULL, false);
```
### 6. 小视频拖拽和绘制
绘制的方法上课讲过了，通过`SetPixel()`函数来实现
拖拽也没有难度，如果在大三上学期选过计算机图形学的同学做这个应该很简单
思路就是获取到初始点击位置的坐标，在每一帧获取一次当前位置，计算出差值后加在当前坐标上即可
```cpp
// 定义参数
// 拖拽参数
int lastX, lastY;                               // 第一次点击时的 X，Y
int nowX, nowY;                                 // 松开鼠标时的X，Y
bool isDrag = false;                            // 是否在拖拽

// 绘制参数
int paintX[1000], paintY[1000];                 // 坐标点
int paintIndex = 0;                             // 绘制索引
bool isPaint = false;                           // 是否在绘制
```
要用到的几个鼠标输入监听
```cpp
case WM_LBUTTONDOWN: // 当用户在光标位于窗口工作区中并且用户按下鼠标左键时发布。 如果未捕获鼠标，则会将消息发布到光标下的窗口。 否则，会将消息发送到捕获了鼠标的窗口。
    hdc2 = GetDC(hWnd);
    if (dragState == DragState::drag) {
        mouseX = GET_X_LPARAM(lParam);
        mouseY = GET_Y_LPARAM(lParam);
        cout << mouseX << " " << mouseY << endl;
        if (mouseX >= x2 && mouseY >= y2 && mouseX <= x2 + width2 && mouseY <= y2 + height2) {
            isDrag = true;
            lastX = mouseX;
            lastY = mouseY;
        }
    }
    if (paintState == PaintState::paint) {
        isPaint = true;
        paintX[paintIndex] = GET_X_LPARAM(lParam);
        paintY[paintIndex] = GET_Y_LPARAM(lParam);
    }
    break;
case WM_MOUSEMOVE: // 监听左键点击后移动
    if (isPaint) {
        hdc2 = GetDC(hWnd);
        paintX[paintIndex] = GET_X_LPARAM(lParam);
        paintY[paintIndex] = GET_Y_LPARAM(lParam);
        paintIndex++;
    }
    if (isDrag) {
        nowX = GET_X_LPARAM(lParam);
        nowY = GET_Y_LPARAM(lParam);
    }
    break;
case WM_LBUTTONUP: // 监听左键抬起
    hdc2 = GetDC(hWnd);
    //拖动小窗
    if (dragState == DragState::drag) {
        if (isDrag) {
            isDrag = false;
        }
    }
    if (paintState == PaintState::paint) {
        isPaint = false;
    }
    break
```
之后只需要在绘制部分进行简单的坐标计算、颜色修改即可

## 多媒体实验完结！
**能做的都做了，希望有个好成绩！**