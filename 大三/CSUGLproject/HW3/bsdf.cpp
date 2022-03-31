#include "primitive.h"
#include "bsdf.h"
#include <random>

namespace csupbr {
    ///////////////////////////
    /// LambertianReflection
    ///////////////////////////
    
    void LambertianReflection::sample(BSDFRecord &bsdfRec, const Point2f &sample, Float &pdf) const {
        // 在这写代码，漫反射
        // ...
        //bsdfRec.wi = bsdfRec.wo;
        Vec3f unit_cos_sampling = cosineHemisphereSampling(sample);
        Vec3f unit_uni_sampling = uniformHemisphereSampling(sample);
        bsdfRec.wi = bsdfRec.isct.shCoord.toWorld(unit_cos_sampling); //余弦
        //bsdfRec.wi = bsdfRec.isct.shCoord.toWorld(unit_uni_sampling); //均匀
        bsdfRec.wi.normalize();
        pdf = this->pdf(bsdfRec);
    }

    Spectrum LambertianReflection::evaluate(BSDFRecord &bsdfRec, Float &pdf) const {
        pdf = this->pdf(bsdfRec);
        return {};
    }

    Float LambertianReflection::pdf(const BSDFRecord &bsdfRec) const {
        return InvPi2;
    }

    ///////////////////////////
    /// SpecularReflection
    ///////////////////////////

    void SpecularReflection::sample(BSDFRecord &bsdfRec, const Point2f &sample, Float &pdf) const {
        // 在这写代码,镜面反射
        // ...
        //bsdfRec.wi = bsdfRec.wo;
        Vec3f reflected = random_in_unit_ball()*roughness + reflect(-bsdfRec.wo, bsdfRec.isct.shN);
        bsdfRec.wi = reflected;
        bsdfRec.wi.normalize();
        pdf = 1.f;
    }

    Spectrum SpecularReflection::evaluate(BSDFRecord &bsdfRec, Float &pdf) const {
        pdf = this->pdf(bsdfRec);
        return {};
    }

    Float SpecularReflection::pdf(const BSDFRecord &bsdfRec) const {
        return .0f;
    }

    ///////////////////////////
    /// SpecularTransmission
    ///////////////////////////

    void SpecularTransmission::sample(BSDFRecord &bsdfRec, const Point2f &sample, Float &pdf) const {
        // 在这写代码，镜面折射
        // ...
        float len = dot(bsdfRec.isct.geoN, bsdfRec.wo);
        float len1 = len * len;
        float refract_rate1 = refract_rate * refract_rate;
        //使用折射公式
        if (dot(bsdfRec.isct.geoN , bsdfRec.isct.shN) < 0) {
            bsdfRec.wi = ((refract_rate * len - sqrt(abs(1 - refract_rate1 * (1 - len1)))) * bsdfRec.isct.geoN - refract_rate * bsdfRec.wo);
        }
        else {
            bsdfRec.wi = ((1 / refract_rate * len - sqrt(1 - 1 / refract_rate1 * (1 - len1))) * bsdfRec.isct.geoN - 1 / refract_rate * bsdfRec.wo);
        }
        //bsdfRec.wi = bsdfRec.wo;
        bsdfRec.wi.normalize();
        pdf = 1.f;
    }

    Spectrum SpecularTransmission::evaluate(BSDFRecord &bsdfRec, Float &pdf) const {
        pdf = this->pdf(bsdfRec);
        return {};
    }

    Float SpecularTransmission::pdf(const BSDFRecord &bsdfRec) const {
        return .0f;
    }
}
