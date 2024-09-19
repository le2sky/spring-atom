package coupon.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
class CouponBenefitStrategyConverter implements AttributeConverter<CouponBenefitStrategy, String> {

    @Override
    public String convertToDatabaseColumn(CouponBenefitStrategy attribute) {
        return attribute.getStrategyName();
    }

    @Override
    public CouponBenefitStrategy convertToEntityAttribute(String dbData) {
        return CouponBenefitStrategyFactory.generate(dbData);
    }
}
