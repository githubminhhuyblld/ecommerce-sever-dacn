package nlu.edu.vn.ecommerce.controllers;


import nlu.edu.vn.ecommerce.exception.ResponseArray;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.address.District;
import nlu.edu.vn.ecommerce.models.address.Province;
import nlu.edu.vn.ecommerce.models.address.Ward;
import nlu.edu.vn.ecommerce.request.address.DistrictRequest;
import nlu.edu.vn.ecommerce.request.address.ProvinceRequest;
import nlu.edu.vn.ecommerce.request.address.WardRequest;
import nlu.edu.vn.ecommerce.services.IDistrictService;
import nlu.edu.vn.ecommerce.services.IProvinceService;
import nlu.edu.vn.ecommerce.services.IWardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

/**
 * @author Minh Huy
 *
 * Address Controller class.
 */
@RestController
@RequestMapping("/api/v1")
public class AddressController {
    @Autowired
    private IProvinceService iProvinceService;
    @Autowired
    private IDistrictService iDistrictService;
    @Autowired
    private IWardService iWardService;

    @GetMapping("/provinces")
    @ApiOperation("Get all provinces")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
            @ApiResponse(code = 404, message = "Provinces not found", response = ResponseObject.class)
    })
    public ResponseEntity<?> getAllProvinces() {
        int total = iProvinceService.getAllProvinces().toArray().length;
        return ResponseEntity.ok().body(
                new ResponseArray(total, "oke", "thành công", iProvinceService.getAllProvinces()));

    }

    @PostMapping("/provinces")
    @ApiOperation("Add province")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
    })
    public Province addProvince(@RequestBody ProvinceRequest request) {
        return iProvinceService.addProvinces(request);

    }
    @GetMapping("{provinceId}/districts")
    @ApiOperation("Get district by provinceId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
            @ApiResponse(code = 404, message = "District not found", response = ResponseObject.class)
    })
    public ResponseEntity<?> getDistrictByProvinceId(@PathVariable("provinceId") String provinceId) {

        return ResponseEntity.ok().body(
                new ResponseObject("oke", "thành công", iDistrictService.getDistrictByProvinceId(provinceId)));

    }
    @PostMapping("{provinceId}/districts")
    @ApiOperation("Add district")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
    })
    public District addDistrict(@PathVariable("provinceId") String provinceId,@RequestBody DistrictRequest request) {
        return iDistrictService.addDistrict(provinceId,request);

    }
    @PostMapping("{districtId}/wards")
    @ApiOperation("Add wards")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
    })
    public Ward addWard(@PathVariable("districtId") String districtId,@RequestBody WardRequest request) {
        return iWardService.addWards(districtId,request);

    }
    @GetMapping("{districtId}/wards")
    @ApiOperation("Get ward by districtId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseObject.class),
    })
    public ResponseEntity<?> getWardByDistrictId(@PathVariable("districtId") String districtId) {

        return ResponseEntity.ok().body(
                new ResponseObject("oke", "thành công", iWardService.getWardsByDistrictId(districtId)));

    }



}
