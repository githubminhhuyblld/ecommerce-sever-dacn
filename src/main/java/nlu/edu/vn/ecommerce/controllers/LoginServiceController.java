package nlu.edu.vn.ecommerce.controllers;

import lombok.extern.log4j.Log4j2;
import nlu.edu.vn.ecommerce.dto.auth.TokenDTO;
import nlu.edu.vn.ecommerce.jwt.JwtHelper;
import nlu.edu.vn.ecommerce.models.auth.RefreshToken;
import nlu.edu.vn.ecommerce.models.enums.ServiceType;
import nlu.edu.vn.ecommerce.models.role.Role;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.repositories.token.RefreshTokenRepository;
import nlu.edu.vn.ecommerce.repositories.role.RoleRepository;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;
import nlu.edu.vn.ecommerce.request.login.GoogleRequest;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("/api/v1/")
@Log4j2
public class LoginServiceController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    private RoleRepository roleRepository;


    @PostMapping("google/login")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleRequest googleRequest) {
        User existingEmail = userRepository.findByEmail(googleRequest.getEmail());
        if (existingEmail != null && existingEmail.getServiceType().equals(ServiceType.GOOGLE)) {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(existingEmail);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(existingEmail);
            String refreshTokenString = jwtHelper.generateRefreshToken(existingEmail, refreshToken);

            return ResponseEntity.ok(new TokenDTO(existingEmail.getId(), accessToken, refreshTokenString));
        }

        User user = new User();
        user.setLastName(googleRequest.getFamilyName());
        user.setFirstName(googleRequest.getGivenName());
        user.setUsername(googleRequest.getEmail());
        user.setEmail(googleRequest.getEmail());
        user.setImage(googleRequest.getImage());
        user.setServiceType(ServiceType.GOOGLE);
        user.setCreateAt(new Timestamp().getTime());
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


}
