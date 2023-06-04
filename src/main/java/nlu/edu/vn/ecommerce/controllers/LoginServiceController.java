package nlu.edu.vn.ecommerce.controllers;

import lombok.extern.log4j.Log4j2;
import nlu.edu.vn.ecommerce.dto.TokenDTO;
import nlu.edu.vn.ecommerce.jwt.JwtHelper;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.repositories.RefreshTokenRepository;
import nlu.edu.vn.ecommerce.repositories.RoleRepository;
import nlu.edu.vn.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@Log4j2
public class LoginServiceController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    private RoleRepository roleRepository;



    @GetMapping("google/login")
    public ResponseEntity<?> user(Principal principal) {

        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) principal;
            OAuth2User oauth2User = authentication.getPrincipal();


            String userId = oauth2User.getAttribute("id");
            String givenName = oauth2User.getAttribute("given_name");
            String familyName = oauth2User.getAttribute("family_name");
            String email = oauth2User.getAttribute("email");
            String pictureUrl = oauth2User.getAttribute("picture");
            String phoneNumber = oauth2User.getAttribute("phone_number");
            String gender = oauth2User.getAttribute("gender");
            GenderType genderType = GenderType.fromString(gender);
            String birthdate = oauth2User.getAttribute("birthdate");


            User existingEmail = userRepository.findByEmail(email);
            if(existingEmail != null){
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setOwner(existingEmail);
                refreshTokenRepository.save(refreshToken);

                String accessToken = jwtHelper.generateAccessToken(existingEmail);
                String refreshTokenString = jwtHelper.generateRefreshToken(existingEmail, refreshToken);

                return ResponseEntity.ok(new TokenDTO(existingEmail.getId(), accessToken, refreshTokenString));
            }

            User user = new User();
            user.setId(userId);
            user.setLastName(familyName);
            user.setFirstName(givenName);
            user.setUsername(email);
            user.setEmail(email);
            user.setImage(pictureUrl);
            user.setNumberPhone(phoneNumber);
            user.setGender(genderType);
            user.setDateOfBirth(birthdate);
            user.setServiceType(ServiceType.GOOGLE);
            List<Role> roles = new ArrayList<>();
            Role roleUser = roleRepository.findByName("ROLE_USER");
            roles.add(roleUser);
            user.setRoles(roles);

            userRepository.save(user);

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user);
            String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenString));
        }


        return null;
    }
    @GetMapping("google/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName());
            if (client != null) {
                authorizedClientService.removeAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName());
            }
        }
        deleteCookie(request, response, "JSESSIONID");

        response.sendRedirect("https://accounts.google.com/logout");
        return "redirect:http://127.0.0.1:5173/login";
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    

}
