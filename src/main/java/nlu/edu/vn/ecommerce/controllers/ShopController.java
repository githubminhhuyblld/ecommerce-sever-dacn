package nlu.edu.vn.ecommerce.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.shop.Shop;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.request.shop.ShopRequest;
import nlu.edu.vn.ecommerce.services.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {
    @Autowired
    private IShopService iShopService;

    @PostMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> registerShop(@RequestParam("userId") String userId, @RequestBody ShopRequest shopRequest) {
        Shop shop = iShopService.registerShop(userId, shopRequest);
        return ResponseEntity.ok().body(new ResponseObject("oke", "đăng ký thành công !", shop));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateShopById(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable String id, @RequestBody ShopRequest shopRequest, @RequestParam("userId") String userId) {
        Shop updatedShop = iShopService.updateShopById(id, shopRequest,userId);
        return ResponseEntity.ok().body(updatedShop);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getShopById(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("id") String id, @RequestParam("userId") String userId) {
        Shop shop = iShopService.getShopById(id);
        if (shop == null) {
            throw new NotFoundException("Không tìm thấy shop!");
        }
        return ResponseEntity.ok().body(new ResponseObject("oke", "thành công", shop));
    }

    @GetMapping("/check-name")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam("name") String name) {
        boolean exists = iShopService.checkExitsName(name);
        return ResponseEntity.ok(exists);
    }
}
