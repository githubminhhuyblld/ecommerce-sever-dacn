package nlu.edu.vn.ecommerce.models.enums;

public enum GenderType {
    MALE,
    FEMALE,
    OTHER;
    public static GenderType fromString(String value) {
        for (GenderType gender : GenderType.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return null;
    }
}
