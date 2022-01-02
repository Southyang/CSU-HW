#include "../core/shape.h"

#include "../core/primitive.h"

namespace csupbr {

    bool Sphere::intersect(const Ray &ray, Intersection &isct) const {
        // 转换到局部坐标
        Ray oRay = (*w2o)(ray);
        // 请使用 'oRay' 而不是 'ray'
        // 请计算以下的值
        float hit_t = 1;                            // 射线t
        Point3f hit_point;                      // 相交点
        Normal geometryNormal = { 0 , 0 , 1.f };
        Normal shadingNormal = { 0 , 0 , 1.f };   // 法向量
        Point2f uv;                             // 纹理坐标
        // ...
        Vec3f oc = oRay.origin;
        float a = dot(oRay.direct, oRay.direct);
        float b = dot(oc, oRay.direct);
        float c = dot(oc, oc) - radius * radius;

        float delta = b * b - a * c; //计算delta
       
       
        if (delta < 0) { //无交点
            return false;
        }
        else { //有交点
            float sqrtd = sqrt(delta); //对delta开方
            float t0 = (-b - sqrtd) / (a);
            float t1 = (-b + sqrtd) / (a);
            float root = t0; //计算数值解
            if (root < 0) {
                if (t1 < 0) {
                    return false;
                }
                else {
                    root = t1;
                }
            }
            if (root > oRay.maxt || root < oRay.mint) { //有交点，但超出范围
                return false;
            }
            hit_t = root; //交点
            hit_point = oRay.to(root);
            geometryNormal = hit_point / radius; //计算法向量，其实radius = 1，可以不写
            shadingNormal = geometryNormal;
            if (dot(oRay.direct, geometryNormal) > 0.0) { // ray is inside the sphere
                geometryNormal = -geometryNormal;
            }
            
        }
        //计算uv坐标
        uv.u = atan2f(hit_point.z, hit_point.x) / 2.f / Pi + 0.5f;
        uv.v = asinf(std::clamp(hit_point.y, -1.f, 1.f)) / Pi;

        // ***以下代码不要动***
        // 转换到世界坐标
        isct.pHit = (*o2w)(hit_point);
        isct.geoN = normalize((*o2w)(geometryNormal));
        isct.shN = normalize((*o2w)(shadingNormal));
        isct.uv = uv;
        isct.geoCoord = CoordinateSystem(isct.geoN);
        isct.shCoord = CoordinateSystem(isct.shN);
        ray.maxt = hit_t;

        // 不要忘记返回是否相交
        return true;
    }

    bool Sphere::intersectP(const Ray &ray, bool blend_test) const {
        // 只需计算是否相交即可 (blend_test不用管)
        // ...
        
        // 转换到局部坐标
        Ray oRay = (*w2o)(ray);
        Vec3f oc = oRay.origin;
        float a = dot(oRay.direct, oRay.direct);
        float b = 2.0 * dot(oc, oRay.direct);
        float c = dot(oc, oc) - radius * radius;

        float delta = b * b - 4 * a * c; //计算delta
        float sqrtd = sqrt(delta); //对delta开方
        float root = (-b - sqrtd) / (2 * a); //计算数值解

        if (delta < 0) { //无交点
            return false;
        }
        else if (root > oRay.maxt || root < oRay.mint) { //有交点，但超出范围
            return false;
        }
        else {
            return true;
        }

        // 不要忘记返回是否相交*/
        return false;
    }
    
    BBox3f Sphere::getBBox() const {
        return (*o2w)(BBox3f({-radius, -radius, -radius}, {radius, radius, radius}));
    }

}
