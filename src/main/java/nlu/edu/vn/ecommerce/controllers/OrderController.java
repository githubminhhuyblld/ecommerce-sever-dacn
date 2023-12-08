package nlu.edu.vn.ecommerce.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import nlu.edu.vn.ecommerce.dto.cart.CartDTO;
import nlu.edu.vn.ecommerce.dto.statistics.OrderStatisticsDTO;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.order.Order;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequestMapping("/api/v1/order")
@RestController
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> order(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody CartDTO cartDTO, @RequestParam String userId) {
        String orderId = iOrderService.order(cartDTO, userId);
        if (orderId != null) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "create order successfully", orderId));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Không có sản phẩm thanh toán", null));

        }
    }

    @GetMapping("/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> getOrdersByUserId(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("userId") String userId,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Order> orders = iOrderService.getOrdersForUser(userId, page, size);
        if (orders == null || orders.isEmpty()) {
            throw new NotFoundException("Order not found for userId " + userId);
        }
        return ResponseEntity.ok().body(new ResponseObject("200", "successfully", orders));
    }


    @GetMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok().body(new ResponseObject("200", "successfully", iOrderService.getAllOrders()));
    }

    @PutMapping("/{orderId}/status/delivered")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateOrderStatusDelivered(@PathVariable String orderId) {
        if (iOrderService.updateOrderStatusDelivered(orderId)) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "update status delivered successfully!", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("oke", "update status delivered failed!", null));

        }
    }

    @PutMapping("/{orderId}/status/shipping")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateOrderStatusShipping(@PathVariable String orderId) {
        if (iOrderService.updateOrderStatusShipping(orderId)) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "update status Shipping successfully!", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("oke", "update status Shipping failed!", null));

        }
    }

    @PutMapping("/{orderId}/status/returned")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateOrderStatusReturned(@PathVariable String orderId) {
        if (iOrderService.updateOrderStatusReturned(orderId)) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "update status Shipping successfully!", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("oke", "update status Shipping failed!", null));

        }
    }

    @PutMapping("/{orderId}/status/canceled")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateOrderStatusCanceled(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable String orderId, @RequestParam String userId) {
        if (iOrderService.updateOrderStatusCanceled(orderId)) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "update status canceled successfully!", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("oke", "update status canceled failed!", null));

        }
    }

    @PutMapping("/{orderId}/status/ready")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateOrderStatusReady(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable String orderId, @RequestParam String userId) {
        if (iOrderService.updateOrderStatusReady(orderId)) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "update status ready successfully!", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("oke", "update status ready failed!", null));

        }
    }

    @GetMapping("/{shopId}/shop")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> getOrdersByShopId(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable String shopId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = iOrderService.findByShopId(shopId, pageable);

        if (!orders.isEmpty()) {
            return ResponseEntity.ok().body(new ResponseObject("ọke", "successfully", orders));
        } else {
            throw new NotFoundException("Order not found !!");
        }
    }

    @GetMapping("/{shopId}/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<Page<Order>> getOrdersByShopIdAndStatus(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable String shopId,
            @RequestParam(defaultValue = "") String orderStatus,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = iOrderService.getOrdersByShopIdAndStatus(shopId, orderStatus, pageable);

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{shopId}/latest-orders")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<Page<Order>> getLatestOrdersByShopId(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable String shopId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Order> orders = iOrderService.findByShopIdOrderByCreatedAtDesc(shopId, page, size);
        return ResponseEntity.ok().body(orders);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> deleteOrderById(@ApiIgnore @AuthenticationPrincipal User user, @RequestParam String userId, @PathVariable("orderId") String id) {
        boolean deleted = iOrderService.deleteOrderById(id);
        return ResponseEntity.ok().body(deleted);
    }
    @GetMapping("/shop/{shopId}/email/{email}")
    public ResponseEntity<?> getOrdersByShopIdAndEmail(@PathVariable String shopId, @PathVariable String email) {
        try {
            List<Order> orders = iOrderService.findOrdersByShopAndEmail(shopId, email);
            return ResponseEntity.ok().body(new ResponseObject("oke","successfullly",orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processing failed: " + e.getMessage());
        }
    }


    @GetMapping("/by-week")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getOrdersByWeek(@ApiIgnore @AuthenticationPrincipal User user,
                                                      @RequestParam String userId,
                                                      @RequestParam String shopId) {
        List<OrderStatisticsDTO> orderStatisticsDTO = iOrderService.getOrdersByWeek(shopId);
        return ResponseEntity.ok().body(orderStatisticsDTO);
    }

    @GetMapping("/by-month")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getOrdersByMonth(@ApiIgnore @AuthenticationPrincipal User user,
                                                      @RequestParam String userId,
                                                      @RequestParam String shopId) {
        List<OrderStatisticsDTO> orderStatisticsDTO = iOrderService.getOrdersByMonth(shopId);
        return ResponseEntity.ok().body(orderStatisticsDTO);
    }

    @GetMapping("/six-month")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getOrdersBySixMonth(@ApiIgnore @AuthenticationPrincipal User user,
                                                       @RequestParam String userId,
                                                       @RequestParam String shopId) {
        List<OrderStatisticsDTO> orderStatisticsDTO = iOrderService.getOrdersBySixMonth(shopId);
        return ResponseEntity.ok().body(orderStatisticsDTO);
    }


}
