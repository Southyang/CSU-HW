// VideoProcDemo_OpenCV.cpp : 定义应用程序的入口点。
//

#include "framework.h"
#include "VideoProcDemo_OpenCV.h"
#include "shobjidl_core.h"
#include <windowsx.h>
#include<iostream>
#include<string>
#include <Commdlg.h>
#include<windows.h>
#include "opencv2/opencv.hpp" 

using namespace std;

#define MAX_LOADSTRING 100

// 全局变量:
HINSTANCE hInst;                                // 当前实例
WCHAR szTitle[MAX_LOADSTRING];                  // 标题栏文本
WCHAR szWindowClass[MAX_LOADSTRING];            // 主窗口类名

cv::Mat img1;                                   // 视频1帧
cv::Mat img2;                                   // 视频2帧
WCHAR FileNameOfVideo1[1024];                   // 视频1的文件路径和文件名
WCHAR FileNameOfVideo2[1024];                   // 视频2的文件路径和文件名
cv::VideoCapture VidCap1;                       // 视频1的读取器
cv::VideoCapture VidCap2;                       // 视频2的读取器

int frameNum1;                                  // 视频1帧数
int frameNum2;                                  // 视频2帧数
int frameNow1 = 0;                              // 视频1当前帧数
int frameNow2 = 0;                              // 视频2当前帧数

// 拖拽参数
int lastX, lastY;                               // 第一次点击时的 X，Y
int nowX, nowY;                                 // 松开鼠标时的X，Y
bool isDrag = false;                            // 是否在拖拽

// 绘制参数
int paintX[1000], paintY[1000];                 // 坐标点
int paintIndex = 0;                             // 绘制索引
bool isPaint = false;                           // 是否在绘制

// 画中画参数
int width2, height2;                            // 视频文件2的长宽
int x2, y2;                                     // 视频文件2的位置
HDC hdc2;                                       // 视频文件2的hdc

enum PlayState
{
    playing, paused, stopped
};
PlayState playState1 = PlayState::playing;       // 播放状态     
PlayState playState2 = PlayState::playing;       // 播放状态  
enum VideoEffect
{
    noEdge, edge
};
VideoEffect vidEffect1 = VideoEffect::noEdge;    // 视频1画面状态
VideoEffect vidEffect2 = VideoEffect::noEdge;    // 视频2画面状态
enum PiPState {
    noPip, pip                                     
};
PiPState pipState = PiPState::noPip;             // 画中画状态
enum DragState {
    noDrag, drag                                     
};
DragState dragState = DragState::noDrag;         // 拖动状态
enum PaintState {
    noPaint, paint                                     
};
PaintState paintState = PaintState::noPaint;     // 绘制状态

// 此代码模块中包含的函数的前向声明:
ATOM                MyRegisterClass(HINSTANCE hInstance);
BOOL                InitInstance(HINSTANCE, int);
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK    About(HWND, UINT, WPARAM, LPARAM);
bool OpenVideoFile(HWND hWnd, LPWSTR* fn);
std::string WCHAR2String(LPCWSTR pwszSrc);
void PlayVideo(cv::Mat img, HDC hdc, HWND hwnd, int x, int y, int width, int height);
void test(bool result, HWND hWnd, WCHAR* fn, cv::VideoCapture VidCap, cv::Mat img, int frameNum);

int APIENTRY wWinMain(_In_ HINSTANCE hInstance,
                     _In_opt_ HINSTANCE hPrevInstance,
                     _In_ LPWSTR    lpCmdLine,
                     _In_ int       nCmdShow)
{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

    // TODO: 在此处放置代码。
    //img1 = cv::imread("d://wallpaper.jpg");

    // 初始化全局字符串
    LoadStringW(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
    LoadStringW(hInstance, IDC_VIDEOPROCDEMOOPENCV, szWindowClass, MAX_LOADSTRING);
    MyRegisterClass(hInstance);

    // 执行应用程序初始化:
    if (!InitInstance (hInstance, nCmdShow))
    {
        return FALSE;
    }

    HACCEL hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_VIDEOPROCDEMOOPENCV));

    MSG msg;

    // 主消息循环:
    while (GetMessage(&msg, nullptr, 0, 0))
    {
        if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    return (int) msg.wParam;
}



//
//  函数: MyRegisterClass()
//
//  目标: 注册窗口类。
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
    WNDCLASSEXW wcex;

    wcex.cbSize = sizeof(WNDCLASSEX);

    wcex.style          = CS_HREDRAW | CS_VREDRAW;
    wcex.lpfnWndProc    = WndProc;
    wcex.cbClsExtra     = 0;
    wcex.cbWndExtra     = 0;
    wcex.hInstance      = hInstance;
    wcex.hIcon          = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_VIDEOPROCDEMOOPENCV));
    wcex.hCursor        = LoadCursor(nullptr, IDC_ARROW);
    wcex.hbrBackground  = (HBRUSH)(COLOR_WINDOW+1);
    wcex.lpszMenuName   = MAKEINTRESOURCEW(IDC_VIDEOPROCDEMOOPENCV);
    wcex.lpszClassName  = szWindowClass;
    wcex.hIconSm        = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

    return RegisterClassExW(&wcex);
}

//
//   函数: InitInstance(HINSTANCE, int)
//
//   目标: 保存实例句柄并创建主窗口
//
//   注释:
//
//        在此函数中，我们在全局变量中保存实例句柄并
//        创建和显示主程序窗口。
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   hInst = hInstance; // 将实例句柄存储在全局变量中

   HWND hWnd = CreateWindowW(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, nullptr, nullptr, hInstance, nullptr);

   if (!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   SetTimer(hWnd, 1, 40, NULL);

   return TRUE;
}

//
//  函数: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  目标: 处理主窗口的消息。
//
//  WM_COMMAND  - 处理应用程序菜单
//  WM_PAINT    - 绘制主窗口
//  WM_DESTROY  - 发送退出消息并返回
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    WCHAR* fn1 = (WCHAR*)FileNameOfVideo1; // 读取视频文件1
    WCHAR* fn2 = (WCHAR*)FileNameOfVideo2; // 读取视频文件2
    bool result;                           // 打开文件操作的返回值
    int mouseX, mouseY;                          // 鼠标点击位置

    switch (message)
    {
    case WM_COMMAND: // 当用户从菜单中选择命令项、控件向其父窗口发送通知消息或翻译快捷键击键时发送。
        {
            int wmId = LOWORD(wParam);
            // 分析菜单选择:
            switch (wmId)
            {
            case IDM_ABOUT:
                DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
                break;
            case IDM_EXIT:
                DestroyWindow(hWnd);
                break;
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
                break;
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
            case IDM_EDGE_EFFECT:
                if (vidEffect1 == VideoEffect::edge) {
                    vidEffect1 = VideoEffect::noEdge;
                }
                else {
                    vidEffect1 = VideoEffect::edge;
                }
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
                break;
            case IDM_EDGE_EFFECT2:
                if (vidEffect2 == VideoEffect::edge) {
                    vidEffect2 = VideoEffect::noEdge;
                }
                else {
                    vidEffect2 = VideoEffect::edge;
                }
                break;
            case IDM_INSERT_VID:
                if (pipState == PiPState::pip) {
                    pipState = PiPState::noPip;
                }
                else {
                    pipState = PiPState::pip;
                }
                break;
            case IDM_DRAG:
                if (dragState == DragState::drag) {
                    dragState = DragState::noDrag;
                }
                else {
                    dragState = DragState::drag;
                }
                break;
            case IDM_MAKE:
                if (paintState == PaintState::paint) {
                    paintState = PaintState::noPaint;
                }
                else {
                    paintState = PaintState::paint;
                }
                break;
            case IDM_CLEAN:
                paintIndex = 0;
                break;
            default:
                return DefWindowProc(hWnd, message, wParam, lParam);
            }
        }
        break;
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
    case WM_MOUSEMOVE:
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
    case WM_LBUTTONUP:
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
        break;
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
    case WM_PAINT: // 当系统或其他应用程序发出请求来绘制应用程序窗口的一部分时，将发送 WMPAINT_ 消息。 
    {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hWnd, &ps);
        // TODO: 在此处添加使用 hdc 的任何绘图代码...
        // 转一下格式 ,这段可以放外面,
        RECT rect;
        GetClientRect(hWnd, &rect);

        HDC hdc1 = CreateCompatibleDC(hdc);
        RECT clientRect;
        GetClientRect(hWnd, &clientRect);
        HBITMAP hBmp = CreateCompatibleBitmap(hdc, clientRect.right, clientRect.bottom); //创建hBmp
        SelectObject(hdc1, hBmp);
        // Rectangle(hdc1, -1, -1, clientRect.right + 2, clientRect.bottom + 2); // 去掉这一步，窗口为黑色背景

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
        else {
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
        }

        if (paintState == PaintState::paint) {
            for (int i = 0; i < paintIndex; i++) {
                SetPixel(hdc1, paintX[i], paintY[i], RGB(255, 255, 255));
            }
        }
        
        BitBlt(hdc, 0, 0, clientRect.right, clientRect.bottom, hdc1, 0, 0, SRCCOPY);
        DeleteObject(hBmp); // 回收内存资源
        DeleteDC(hdc1);
           
        EndPaint(hWnd, &ps);
        }
        break;
    case WM_DESTROY: // 销毁窗口时发送。 从屏幕中删除窗口后，该窗口将发送到要销毁的窗口的窗口过程。
        PostQuitMessage(0);
        break;
    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

// “关于”框的消息处理程序。
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    UNREFERENCED_PARAMETER(lParam);
    switch (message)
    {
    case WM_INITDIALOG:
        return (INT_PTR)TRUE;

    case WM_COMMAND:
        if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
        {
            EndDialog(hDlg, LOWORD(wParam));
            return (INT_PTR)TRUE;
        }
        break;
    }
    return (INT_PTR)FALSE;
}

bool OpenVideoFile(HWND hWnd, LPWSTR* fn)
{
    IFileDialog* pfd = NULL;
    HRESULT hr = CoCreateInstance(CLSID_FileOpenDialog,
        NULL,
        CLSCTX_INPROC_SERVER,
        IID_PPV_ARGS(&pfd));

    DWORD dwFlags;
    hr = pfd->GetOptions(&dwFlags);
    hr = pfd->SetOptions(dwFlags | FOS_FORCEFILESYSTEM);

    COMDLG_FILTERSPEC rgSpec[] =
    {
        { L"MP4", L"*.mp4" },
        { L"AVI", L"*.avi" },
        { L"ALL", L"*.*" },
    };

    HRESULT SetFileTypes(UINT cFileTypes, const COMDLG_FILTERSPEC * rgFilterSpec);
    hr = pfd->SetFileTypes(ARRAYSIZE(rgSpec), rgSpec);
    hr = pfd->SetFileTypeIndex(1);

    hr = pfd->Show(hWnd);///显示打开文件对话框

    IShellItem* pShellItem = NULL;
    if (SUCCEEDED(hr))
    {
        hr = pfd->GetResult(&pShellItem);
        hr = pShellItem->GetDisplayName(SIGDN_DESKTOPABSOLUTEPARSING, fn);//获取文件的完整路径

        return true;
    }

    return false;

}

std::string WCHAR2String(LPCWSTR pwszSrc)
{
    int nLen = WideCharToMultiByte(CP_ACP, 0, pwszSrc, -1, NULL, 0, NULL, NULL);
    if (nLen <= 0)
        return std::string("");

    char* pszDst = new char[nLen];
    if (NULL == pszDst)
        return std::string("");

    WideCharToMultiByte(CP_ACP, 0, pwszSrc, -1, pszDst, nLen, NULL, NULL);
    pszDst[nLen - 1] = 0;

    std::string strTmp(pszDst);
    delete[] pszDst;

    return strTmp;
}

void PlayVideo(cv::Mat img, HDC hdc, HWND hwnd, int x, int y, int width, int height) {
    if (img.rows > 0)
    {
        switch (img.channels())
        {
        case 1:
            cv::cvtColor(img, img, cv::COLOR_GRAY2BGR); // GRAY单通道
            break;
        case 3:
            cv::cvtColor(img, img, cv::COLOR_BGR2BGRA);  // BGR三通道
            break;
        default:
            break;
        }

        int pixelBytes = img.channels() * (img.depth() + 1); // 计算一个像素多少个字节

                                                             // 制作bitmapinfo(数据头)
        BITMAPINFO bitInfo;
        bitInfo.bmiHeader.biBitCount = 8 * pixelBytes;
        bitInfo.bmiHeader.biWidth = img.cols;
        bitInfo.bmiHeader.biHeight = -img.rows;
        bitInfo.bmiHeader.biPlanes = 1;
        bitInfo.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
        bitInfo.bmiHeader.biCompression = BI_RGB;
        bitInfo.bmiHeader.biClrImportant = 0;
        bitInfo.bmiHeader.biClrUsed = 0;
        bitInfo.bmiHeader.biSizeImage = 0;
        bitInfo.bmiHeader.biXPelsPerMeter = 0;
        bitInfo.bmiHeader.biYPelsPerMeter = 0;
        // Mat.data + bitmap数据头 -> MFC

        StretchDIBits(
            hdc,
            x, y, width, height,
            0, 0, img.cols, img.rows,
            img.data,
            &bitInfo,
            DIB_RGB_COLORS,
            SRCCOPY
        );
    }
}
//————————————————
//版权声明：本文为CSDN博主「kingkee」的原创文章，遵循CC 4.0 BY - SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https ://blog.csdn.net/kingkee/java/article/details/98115024