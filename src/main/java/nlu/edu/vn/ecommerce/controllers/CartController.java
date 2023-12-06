package nlu.edu.vn.ecommerce.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import nlu.edu.vn.ecommerce.exception.ErrorException;
import nlu.edu.vn.ecommerce.exception.MyException;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.cart.Cart;
import nlu.edu.vn.ecommerce.models.cart.CartItem;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private ICartService iCartService;



    @GetMapping("/{userId}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getCartByUserId(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("userId") String userId) {
        List<Cart> carts = iCartService.getCartByUserId(userId);
        if (!carts.isEmpty()) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", carts));
        } else {
            throw new NotFoundException("Không có sản phẩm trong giỏ hàng");
        }


    }

    @PostMapping("/add")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> addToCart(@ApiIgnore @AuthenticationPrincipal User user, @RequestParam("userId") String userId, @RequestBody CartItem cartItem) {
        iCartService.addToCart(userId, cartItem);
        return ResponseEntity.ok().body(new MyException(HttpStatus.OK, "Thêm sản phẩm vào giở hàng thành công"));
    }
    @PutMapping("/update/items/{productId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> updateCartItemQuantity(@ApiIgnore @AuthenticationPrincipal User user,
                                                    @PathVariable String productId,
                                                    @RequestParam(name ="userId",required = false ) String userId,
                                                    @RequestParam(name="amount",required = false) int amount) {
        CartItem item = iCartService.updateCartItemQuantityByProductIdAndUserId(productId, userId, amount);
        if (item == null) {
            return ResponseEntity.badRequest().body(new ErrorException(HttpStatus.NOT_FOUND, "update số lượng thất bại"));
        } else {
            return ResponseEntity.ok().body(new MyException(HttpStatus.FOUND, "update số lượng thành công"));
        }
    }
    @DeleteMapping("/remove/{userId}/items/{productId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> removeItemFromCart(@ApiIgnore @AuthenticationPrincipal User user,
                                                @PathVariable("userId") String userId,
                                                @PathVariable String productId) {
        boolean removed = iCartService.removeItemFromCart(userId, productId);
        if (removed) {
            return ResponseEntity.ok().body(new MyException(HttpStatus.OK, "Xóa sản phẩm khỏi giở hàng thành công"));

        } else {
            return ResponseEntity.badRequest().body(new NotFoundException("Không tìm thấy item trong giỏ hàng"));
        }
    }
}
