package nlu.edu.vn.ecommerce.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import nlu.edu.vn.ecommerce.models.Color;
import nlu.edu.vn.ecommerce.models.Image;
import nlu.edu.vn.ecommerce.models.Size;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String name;

    private String mainImage;

    @JsonProperty(required = false)
    private List<Image> images;

    @JsonProperty(required = false)
    private List<Size> sizes;

    @JsonProperty(required = false)
    private List<Color> colors;

    private BigDecimal newPrice;

    @JsonProperty(required = false)
    private BigDecimal oldPrice;

    private String description;

    private int quantity;

    @JsonProperty(required = false)
    private int sale;

    private String categoryId;
}
