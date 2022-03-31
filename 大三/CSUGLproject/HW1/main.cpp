#include "primitive.h"

#include <imgui.h>
#include <backends/imgui_impl_glfw.h>
#include <backends/imgui_impl_opengl3.h>

// 鼠标拖动方法
// winSize: 当前窗口大小
// primitive: 当前拖拽的图元
void MouseDragger(const glm::ivec2& winSize, csugl::Ref<Primitive> primitive);

// 窗口坐标转换为NDC坐标
// winPosition: 待转换的坐标
// winSize: 当前窗口大小
// return: 转换后的NDC坐标
glm::vec2 WindowPosToNDCPos(const glm::ivec2& winPosition, const glm::ivec2& winSize);

int flag = 0;
bool firstdragpos = false;
bool changepos = false;
glm::vec2 lpos; //距离

int main(int argc, char const *argv[]) {
    // glog 初始化
    FLAGS_stderrthreshold = 0;
    google::InitGoogleLogging(argv[0]);

    // 初始化应用程序单例，并获取实例
    auto app = csugl::singleton<csugl::Application>::getInstance();
    
    // 获取窗口
    auto window = &app->GetWindow();
    // 开启垂直同步
    window->SetVSync(true);
    // 获取窗口大小
    glm::ivec2 winSize = window->GetSize();
    // 窗口置顶
    glfwSetWindowAttrib(window->GetGLFWwindow(), GLFW_FLOATING, true);
    // 添加事件回调 (lambda写法)
    window->addEventCallback([&](csugl::Event &ev) {
        csugl::EventDispatcher dispatcher(ev);
        // resize事件(当窗口大小改变时会调用此函数)
        dispatcher.Dispatch<csugl::WindowResizeEvent>([&](csugl::WindowResizeEvent &ev){
            LOG(INFO) << ev;
            // 更新winSize
            winSize = {ev.width, ev.height};
            return false;
        });
    });
    // 重新设置窗口大小
    window->Resize(600, 600);
    
    // 加载shader, 用于绘制最简单的2D图像
    auto shader = csugl::MakeRef<csugl::Shader>("../assets/shader/csucg_assignment1.glsl");

    // 创建一个三角形
    auto triangle = csugl::MakeRef<Triangle>();
    triangle->position = {0.0f, 0.0f};
    triangle->color = {0.8f, 0.5f, 0.1f};
    
    // 创建一个矩形
    auto rectangle = csugl::MakeRef<Rectangle>();
    rectangle->position = {0.0f, 0.0f};
    rectangle->color = {0.8f, 0.5f, 0.1f};

    // 创建一个圆
    auto circle = csugl::MakeRef<Circle>();
    circle->position = {0.0f, 0.0f};
    circle->color = {0.8f, 0.5f, 0.1f};

    // 补充图形
    auto addition = csugl::MakeRef<Addition>();
    addition->position = { 0.0f, 0.0f };
    addition->color = { 0.8f, 0.5f, 0.1f };

    // 渲染对象, 默认为三角形
    csugl::Ref<Primitive> renderObj = triangle;

    // 创建两类光标
    auto normalCur = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    auto dragCur = glfwCreateStandardCursor(GLFW_POINTING_HAND_CURSOR);
    // 默认光标为箭头
    glfwSetCursor(window->GetGLFWwindow(), normalCur);

    ImGui::CreateContext();
    ImGuiIO &io = ImGui::GetIO();
    (void)io;
    io.IniFilename = nullptr;
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;       // Enable Keyboard Controls
    io.ConfigFlags |= ImGuiConfigFlags_DockingEnable;           // Enable Docking

    ImGui::StyleColorsDark();
    ImGui_ImplGlfw_InitForOpenGL(window->GetGLFWwindow(), true);
    ImGui_ImplOpenGL3_Init("#version 330");

    ImGui::LoadIniSettingsFromDisk("../assets/imgui/imgui.ini");

    // 主循环
    while(app->isOpen()) {
        // 设置GL的刷新clear模式
        glClear(GL_COLOR_BUFFER_BIT);
        // 设置刷新颜色
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        
        ImGui_ImplOpenGL3_NewFrame();
        ImGui_ImplGlfw_NewFrame();
        ImGui::NewFrame();

        ImGui::Begin("Input Event");

        // 数字键切换绘制的图形
        if(csugl::Input::IsKeyPressed(csugl::Key::D1)) {        // 1.三角形
            triangle->position = renderObj->position;
            triangle->color = renderObj->color;
            renderObj = triangle;
            ImGui::Text("Key: 'Num1' Pressing...");
        } else if(csugl::Input::IsKeyPressed(csugl::Key::D2)) { // 2.矩形
            rectangle->position = renderObj->position;
            rectangle->color = renderObj->color;
            renderObj = rectangle;
            ImGui::Text("Key: 'Num2' Pressing...");
        } else if(csugl::Input::IsKeyPressed(csugl::Key::D3)) { // 3.圆
            circle->position = renderObj->position;
            circle->color = renderObj->color;
            renderObj = circle;
            ImGui::Text("Key: 'Num3' Pressing...");
        } else if (csugl::Input::IsKeyPressed(csugl::Key::D4)) { // 4 .平行四边形
            addition->position = renderObj->position;
            addition->color = renderObj->color;
            renderObj = addition;
            ImGui::Text("Key: 'Num4' Pressing...");
        }

        // 设置光标类型
        if(csugl::Input::IsMouseButtonPressed(csugl::Mouse::Button0)) {
            glfwSetCursor(window->GetGLFWwindow(), dragCur);            // 点击
            ImGui::Text("Mouse: 'Button0' Pressing...");
        }
        else
            glfwSetCursor(window->GetGLFWwindow(), normalCur);          // 箭头

        // 鼠标拖拽逻辑
        MouseDragger(winSize, renderObj);

        // 按R重置图元坐标
        if(csugl::Input::IsKeyPressed(csugl::Key::R)) {
            ImGui::Text("Key: 'R' Pressing...");
            renderObj->position = {0.0f, 0.0f};
        }

        // 绘制图元
        renderObj->draw(shader);

        ImGui::End();

        ImGui::Render();
        ImGui_ImplOpenGL3_RenderDrawData(ImGui::GetDrawData());

        // 窗口显示（交换缓冲区、处理事件）
        window->Display();
    }

    // 释放资源
    ImGui_ImplOpenGL3_Shutdown();
    ImGui_ImplGlfw_Shutdown();
    ImGui::SaveIniSettingsToDisk("../assets/imgui/imgui.ini");
    ImGui::DestroyContext();

    glfwDestroyCursor(normalCur);
    glfwDestroyCursor(dragCur);
    csugl::singleton<csugl::Application>::destroy();

    return 0;
}

void MouseDragger(const glm::ivec2& winSize, csugl::Ref<Primitive> primitive) {
    // 鼠标在图元内
    static bool isContained = false;
    // 鼠标正在拖拽
    static bool isDraging = false;

    // 1.获取鼠标窗口坐标:
    glm::vec2 pos; //鼠标位置
    
    pos.x = csugl::Input::GetMousePosX();
    pos.y = csugl::Input::GetMousePosY();
    // 2.转换为NDC坐标: (使用WindowPosToNDCPos转换)
    pos = WindowPosToNDCPos(pos, winSize);

    // 3.是否在拖拽:
    if (csugl::Input::IsMouseButtonPressed(csugl::Mouse::Button0)) {
        if (flag == 0) { //首次拖动
            glm::vec2 ppos = primitive->position; //获取图圆当前坐标 
            lpos = ppos - pos; //计算图圆和鼠标坐标距离

            if (isContained) { //在图圆内拖动
                changepos = true;
                firstdragpos = false;
            }
            else { //不在图圆内
                firstdragpos = true;
            }
        }
        isDraging = true;
        flag = 1;
    }
    else {
        changepos = false;
        isDraging = false;
        flag = 0;
    }

    // 4.是否在图元内: (使用primitive的is_contained检测)
    if (primitive->is_contained(pos)) {
        if (flag == 1 && firstdragpos == true) {
            isContained = false;
        }
        else isContained = true;
    }
    else {
        isContained = false;
    }

    // 5.设置图元颜色:
    if (isContained) {
        primitive->color = { 0.9f , 0.9f , 0.9f };
    }
    else {
        primitive->color = { 0.8f , 0.5f , 0.1f };
    }

    // 6.设置图元新坐标:
    if (isDraging) {
        if (changepos) {
            pos.x = pos.x + lpos.x;
            pos.y = pos.y + lpos.y;
            primitive->position = pos;
        }
    }
}

// 将窗口坐标转换为标准设备坐标(NDC)
glm::vec2 WindowPosToNDCPos(const glm::ivec2& winPosition, const glm::ivec2& winSize) {
    float pos[2];
    
    pos[0] = (float)winPosition.x / (float)winSize.x * 2.0 -1.0;
    pos[1] = -(float)winPosition.y / (float)winSize.y * 2.0 + 1.0;
    /*printf("%f ", pos[0]);
    printf("%f\n", pos[1]);*/
    return { pos[0],pos[1] };
}