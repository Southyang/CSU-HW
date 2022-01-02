#include <csugl.h>

// 纹理
struct Texture2D {
    /**
     * 从文件加载纹理
     * file_mame
     */
    Texture2D(const std::string &file_name);

    // 绑定当前纹理
    void bind() const;

    // 长, 宽, 通道数
    int width, height, channel;
    uint32_t tex_id;

};
