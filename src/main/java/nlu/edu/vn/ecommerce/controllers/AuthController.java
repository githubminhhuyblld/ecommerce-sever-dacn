package nlu.edu.vn.ecommerce.controllers;

import nlu.edu.vn.ecommerce.dto.auth.TokenDTO;
import nlu.edu.vn.ecommerce.exception.ErrorException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.jwt.JwtHelper;
import nlu.edu.vn.ecommerce.models.auth.RefreshToken;
import nlu.edu.vn.ecommerce.models.role.Role;
import nlu.edu.vn.ecommerce.models.enums.ServiceType;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.repositories.token.RefreshTokenRepository;
import nlu.edu.vn.ecommerce.repositories.role.RoleRepository;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;
import nlu.edu.vn.ecommerce.request.login.LoginRequest;
import nlu.edu.vn.ecommerce.request.register.SignupRequest;
import nlu.edu.vn.ecommerce.services.UserService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @PostMapping("login")
    @Transactional
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user);
            String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenString));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorException(HttpStatus.UNAUTHORIZED, "unauthorized"));
        }
    }

    @PostMapping("signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseObject("ALL_ERROR", "Error", bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseObject("USERNAME_FOUNDED", "Tài khoản đã tồn tại", null));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseObject("EMAIL_FOUNDED", "Email đã tồn tại", null));
        }
        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getFirstName(), request.getLastName(), request.getNumberPhone());
        List<Role> roles = new ArrayList<>();
        Role roleUser = roleRepository.findByName("ROLE_USER");
        roles.add(roleUser);
        user.setRoles(roles);
        user.setServiceType(ServiceType.NORMAL);
        user.setCreateAt(new Timestamp().getTime());

        userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(user);

            return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user);
            String newRefreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }
}
