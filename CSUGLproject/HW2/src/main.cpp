#include <string>

#include <glad/glad.h>
#include <glog/logging.h>

#include "transform.h"
#include "primitive.h"
#include "camera2D.h"
#include "texture2D.h"

int main(int argc, char const *argv[]) {
    FLAGS_stderrthreshold = 0;
    google::InitGoogleLogging(argv[0]);

    auto app = csugl::singleton<csugl::Application>::getInstance();
    auto window = &app->GetWindow();
    window->Resize(600, 600);

    auto shader = csugl::MakeRef<csugl::Shader>("../assets/shader/csucg_assignment2.glsl");

    // 从本地加载纹理，可以替换成自己的图片
    auto texture_day = csugl::MakeRef<Texture2D>("../assets/texture/dog.png");
    auto texture_night = csugl::MakeRef<Texture2D>("../assets/texture/study.png");

    // 物体的世界变换
    Transform trans(glm::vec3{-0.6f, 0.0f, -0.01f},                   // 平移
                     glm::vec3{0.0f, 0.0f, 1.0f}, glm::radians(0.0f), // 旋转
                     glm::vec3{1.0f}                                  // 缩放
    );
    Transform trans1(glm::vec3{0.6f, 0.0f, 0.0f},                     // 平移
                     glm::vec3{0.0f, 0.0f, 1.0f}, glm::radians(0.0f), // 旋转
                     glm::vec3{1.0f}                                  // 缩放
    );

    // 四边形
    auto quad = csugl::MakeRef<Quad>();
    // 或者使用带参数的Quad构造，目的是生成与texture_day长宽比相同的四边形，这取决于你自己的纹理
    // auto quad1 = csugl::MakeRef<Quad>(texture_day->width, texture_day->height);
    
    // 圆
    auto circle = csugl::MakeRef<Circle>();

    // 图元指针
    csugl::Ref<Primitive> primitive = quad;

    // 创建正交相机
    Camera2D camera2D(glm::vec3{0.0f, 0.0f, 1.0f}, // look from
                      glm::vec3{0.0f},             // look at
                      glm::vec3{0.0f, 1.0f, 0.0f}, // up
                      glm::vec2{2.0f},       // size
                      0.01f, 100.0f                // near far
    );

    // 相机移动速度
    float camera_move_speed = 0.5f;
    // 相机缩放变化速度
    float camera_zoom_speed = 5.0f;
    // 物体旋转速度
    float rotate_speed = 30.0f;

    csugl::Time::init();

    // 添加事件
    window->addEventCallback([&](csugl::Event& ev){
        csugl::EventDispatcher dis(ev);
        dis.Dispatch<csugl::MouseScrolledEvent>([&](csugl::MouseScrolledEvent& ev){
            // 利用鼠标滚轮控制相机zoom缩放
            camera2D.size -= glm::vec2{ev.vertical * csugl::Time::deltaTime()} * camera_zoom_speed;
            return false;
        });
        dis.Dispatch<csugl::KeyPressedEvent>([&](csugl::KeyPressedEvent& ev){
            // 重置相机缩放和位置
            if(ev.keyCode == csugl::Key::R) {
                camera2D.look_from = {0.0f, 0.0f, 1.0f};
                camera2D.look_at = glm::vec3{0.0f};
                camera2D.size = glm::vec2{2.0f};
            }
            // 数字键1 绘制四边形
            if(ev.keyCode == csugl::Key::D1) {
                primitive = quad;
            }
            // 数字键2 绘制圆
            if(ev.keyCode == csugl::Key::D2) {
                primitive = circle;
            }
            return false;
        });
    });

    // 预设渲染状态
    {
        // 多边形渲染模式
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        // 允许深度测试
        glEnable(GL_DEPTH_TEST);
        // 允许混合 (即允许透明度)
        glEnable(GL_BLEND);
        // 设置混合方式
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    while (app->isOpen()) {
        // 获取帧间时间
        float deltaTime = csugl::Time::deltaTime();
        // 旋转物体
        trans.angle += glm::radians(deltaTime * rotate_speed);
        trans1.angle -= glm::radians(deltaTime * rotate_speed);

        // 使用键盘 WASD 移动相机
        if (csugl::Input::IsKeyPressed(csugl::Key::W)) {
            camera2D.look_from.y += deltaTime * camera_move_speed;
            camera2D.look_at.y += deltaTime * camera_move_speed;
        }
        if (csugl::Input::IsKeyPressed(csugl::Key::A)) {
            camera2D.look_from.x -= deltaTime * camera_move_speed;
            camera2D.look_at.x -= deltaTime * camera_move_speed;
        }
        if (csugl::Input::IsKeyPressed(csugl::Key::S)) {
            camera2D.look_from.y -= deltaTime * camera_move_speed;
            camera2D.look_at.y -= deltaTime * camera_move_speed;
        }
        if (csugl::Input::IsKeyPressed(csugl::Key::D)) {
            camera2D.look_from.x += deltaTime * camera_move_speed;
            camera2D.look_at.x += deltaTime * camera_move_speed;
        }

        // 渲染pass
        {
            glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader->use();
            // 相机变换的两个矩阵
            shader->set_mat4("_view", camera2D.get_view_mat());
            shader->set_mat4("_projection", camera2D.get_projection_mat());
            
            // xray要用到的一些参数
            shader->set_vec2("_mousepos", {csugl::Input::GetMousePosX(),
                                           window->GetSize().y - csugl::Input::GetMousePosY()});
            shader->set_float("_range", 50.0f);

            // 激活纹理单元0
            glActiveTexture(GL_TEXTURE0);
            // 绑定纹理
            texture_day->bind();
            shader->set_sampler2D("_inside", 0);

            // 激活纹理单元1
            glActiveTexture(GL_TEXTURE0 + 1);
            // 绑定纹理
            texture_night->bind();
            shader->set_sampler2D("_outside", 1);

            // 绘制
            primitive->draw(shader, trans);
            primitive->draw(shader, trans1);
        }

        window->Display();
        csugl::Time::update();
    }

    csugl::singleton<csugl::Application>::destroy();

    return 0;
}