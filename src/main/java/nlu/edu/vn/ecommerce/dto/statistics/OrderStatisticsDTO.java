package nlu.edu.vn.ecommerce.dto.statistics;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.order.Order;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderStatisticsDTO {
    private String fromTo;
    private List<Order> soldOrder;
    private List<Order> cancelOrder;
    private BigDecimal totalSale;
}
