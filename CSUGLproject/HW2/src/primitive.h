#include <csugl.h>

#include <glad/glad.h>

struct Transform;

struct Primitive {
    Primitive() {
        //
    }

    virtual void draw(csugl::Ref<csugl::Shader> shader, const Transform& trans) const = 0;

    virtual ~Primitive() {}

protected:
    uint32_t vao;
};

// 四边形
struct Quad : public Primitive {
    Quad();

    Quad(int width, int height);

    ~Quad() override;

    // 绘制四边形
    void draw(csugl::Ref<csugl::Shader> shader, const Transform& trans) const override;
    
private:
    uint32_t vbo;
    uint32_t ibo;
    // 按 宽/长 比例生成顶点数据
    void init(float ratio);

};

// 圆
struct Circle : public Primitive {
    Circle();

    ~Circle() override;

    void draw(csugl::Ref<csugl::Shader> shader, const Transform& trans) const override;
    
private:
    uint32_t vbo;
    uint32_t ibo;
    // 圆周上的顶点个数
    const int vertiecs_num = 40;
};