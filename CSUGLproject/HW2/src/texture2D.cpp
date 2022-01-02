#include "texture2D.h"

#include <glad/glad.h>
#include <glog/logging.h>
#include <stb_image.h>

Texture2D::Texture2D(const std::string &file_name) {
    // 1. 使用 stbi 从 file_name 加载图像数据
    stbi_set_flip_vertically_on_load(true);
    auto img_data = stbi_load(file_name.c_str(), &width, &height, &channel, 0);
    CHECK(img_data) << "Texture: file '" << file_name << "' not exist";
    // 2. 生成 texture object, 请使用tex_id
    glGenTextures(1, &tex_id);
    // 3. 绑定当前纹理
    glBindTexture(GL_TEXTURE_2D, tex_id);
    // 4. 设置纹理参数 (提示: 过滤模式请使用 GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    // 5. 拷贝图像数据到显存 (提示: 通道数取决于channel)
    if (channel == 3) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, img_data);
    }
    else {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, img_data);
    }
    // 6. 生成 MipMap
    glGenerateMipmap(GL_TEXTURE_2D);
    // 7. 释放内存
    stbi_image_free(img_data);
}

void Texture2D::bind() const {
	glBindTexture(GL_TEXTURE_2D, tex_id);
}