package nlu.edu.vn.ecommerce.controllers;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

@CrossOrigin(origins = "*", maxAge = 7200)// 2 hours.
@RestController
@RequestMapping("/api/v1/languages")
public class LanguageController {
    @ApiOperation(value = "Change language")
    @GetMapping("/changeLanguage")
    public LinkedHashMap<String, Object> changeLocale(@RequestParam("locale") String locale) {
        Locale localeParam = new Locale(locale);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("lang", localeParam); // lang.properties
        for (String key : resourceBundle.keySet()) {
            result.put(key,
                    resourceBundle.getString(key)); // key = "hello", value = "Xin chào"
        }
        return result;
    }
}

