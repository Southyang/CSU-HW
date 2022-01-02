#include <csugl.h>

// 正交相机 (for 2D)
struct Camera2D {
    Camera2D(
        glm::vec3 look_from, // 自身位置
        glm::vec3 look_at,   // 观察点
        glm::vec3 up,        // 上方向
        glm::vec2 size,      // 视口大小
        float _near,         // 近平面
        float _far           // 远平面
        )
        : look_from(look_from), look_at(look_at), up(up), size(size), _near(_near), _far(_far) {
    }

    glm::mat4 get_view_mat()  const {
        // 1.计算 front 方向
        glm::vec3 cameraDirection = glm::normalize(look_from - look_at);
        // 2.根据 up 和 front 计算 right
        glm::vec3 up = glm::vec3(0.0f, 1.0f, 0.0f);
        glm::vec3 cameraRight = glm::normalize(glm::cross(up, cameraDirection));
        // 3.根据 right 和 front 计算 实际的up 并 传入 glm::lookAt
        glm::vec3 cameraUp = glm::cross(cameraDirection, cameraRight);
        glm::mat4 view;
        view = glm::lookAt(look_from,
            look_at,
            cameraUp);
        return view;
    }

    glm::mat4 get_projection_mat() const{
        // 根据 size 和 near,far , 使用 glm::ortho 计算投影矩阵并反返回
        glm::mat4 view = glm::ortho(size.x, -size.x, size.y, -size.y, _near, _far);
        return view;
    }
    
    glm::vec3 look_from;
    glm::vec3 look_at;
    glm::vec3 up;
    glm::vec2 size;
    float _near, _far;
};
