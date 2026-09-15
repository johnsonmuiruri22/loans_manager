package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUDIT.api.RegisterLogEvent;
import com.loan_manager_app.loans_manager.AUTH.dto.LoginResponse;
import com.loan_manager_app.loans_manager.AUTH.dto.RegRequest;
import com.loan_manager_app.loans_manager.AUTH.events.CurrentUserResponse;
import com.loan_manager_app.loans_manager.AUTH.events.RefreshTokenSession;
import com.loan_manager_app.loans_manager.AUTH.mapper.CurrentUserMapper;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.UserRepository;
import com.loan_manager_app.loans_manager.AUTH.dto.LoginRequest;
import com.loan_manager_app.loans_manager.AUTH.security.CustomUserDetails;
import com.loan_manager_app.loans_manager.AUTH.security.JwtCookieService;
import com.loan_manager_app.loans_manager.AUTH.service.AuthService;
import com.loan_manager_app.loans_manager.AUTH.service.ClientInfoService;
import com.loan_manager_app.loans_manager.AUTH.service.JwtService;
import com.loan_manager_app.loans_manager.AUTH.service.RefreshTokenService;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.Role;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.Status;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.InvalidRefreshTokenException;
import com.loan_manager_app.loans_manager.SHARED.GlobalEvents.ClientInfo;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfo;
import com.loan_manager_app.loans_manager.SHARED.deviceInfo.DeviceInfoExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServImpl implements AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCookieService jwtCookieService;
    private final RefreshTokenService refreshTokenService;
    private final ClientInfoService clientInfoService;
    private final ApplicationEventPublisher eventPublisher;
    private final DeviceInfoExtractor deviceInfoExtractor;


    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        assert principal != null;
        Users appUser = principal.getDomainUser();
        ClientInfo clientInfo = clientInfoService.extractClientInfo(request);

        RefreshTokenSession session = refreshTokenService.createSession(
                appUser,clientInfo.deviceName(),clientInfo.ipAddress()
        );

        String accessToken = jwtService.generateAccessToken(
                principal,
                session.refreshToken().getSessionId());

        jwtCookieService.addAccessTokenCookie(response, accessToken);
        System.out.println("LOGIN RAW TOKEN:");
        System.out.println(session.rawToken());
        jwtCookieService.addRefreshTokenCookie(response, session.rawToken());
        System.out.println("Step 7: Cookies added to response");
        eventPublisher.publishEvent(
                new RegisterLogEvent("User logged in", LogType.LOGGED_IN, deviceInfo)
        );

        return LoginResponse.builder()
                .message("Login successful")
                .role(principal.getDomainUser().getRole().name())
                .userId(principal.getDomainUser().getUserId())
                .username(principal.getUsername())
                .deviceName(clientInfo.deviceName())
                .ipAddress(clientInfo.ipAddress())
                .build();
    }

    @Transactional
    @Override
    public void logout(HttpServletRequest request,
            HttpServletResponse response) {
        jwtCookieService.extractRefreshToken(request)
                .ifPresent(refreshTokenService::revoke);

        jwtCookieService.clearAccessTokenCookie(response);
        jwtCookieService.clearRefreshTokenCookie(response);
        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent("User logged out", LogType.LOGGED_OUT, deviceInfo)
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Void> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        System.out.println("Step 1: Entered refresh endpoint");

        String rawRefreshToken = jwtCookieService
                .extractRefreshToken(request)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                "Refresh token not found."
                        ));

        System.out.println("Step 2: Refresh cookie extracted");
        System.out.println(rawRefreshToken);

        ClientInfo clientInfo = clientInfoService.extractClientInfo(request);

        RefreshTokenSession session =
                refreshTokenService.rotate(rawRefreshToken,clientInfo);

        System.out.println("Step 3: Token rotated");

        Users userToGet = session.refreshToken().getUser();

        CustomUserDetails principal =
                CustomUserDetails.builder()
                        .user(userToGet)
                        .build();

        String accessToken =
                jwtService.generateAccessToken(
                        principal,
                        session.refreshToken().getSessionId()
                );

        jwtCookieService.addAccessTokenCookie(
                response,
                accessToken
        );

        jwtCookieService.addRefreshTokenCookie(
                response,
                session.rawToken()
        );

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent("refresh token refreshed by user",
                        LogType.LOGGED_IN, deviceInfo)
        );
        return ResponseEntity.noContent().build();
    }



    @Override
    public AppResponse RegisterUser(RegRequest regRequest,
                                    HttpServletRequest request) {
        boolean userExists = userRepository.existsByUsername(regRequest.getUsername());
        if (userExists) {
            return AppResponse.builder()
                    .message("User already exists")
                    .statusCode(409)
                    .build();
        }
        Users newUser = Users.builder()
                .username(regRequest.getUsername())
                .otherNames(regRequest.getOtherNames())
                .surname(regRequest.getSurname())
                .password(passwordEncoder.encode(regRequest.getPassword()))
                .role(Role.USER)
                .status(Status.INACTIVE)
                .build();
        userRepository.save(newUser);

        DeviceInfo deviceInfo = deviceInfoExtractor.extract();
        eventPublisher.publishEvent(
                new RegisterLogEvent("User registered by an anonymous user", LogType.REGISTERED,
                        deviceInfo)
        );
        return AppResponse.builder()
                .message("User registered successfully")
                .statusCode(200)
                .build();
    }

    @Override
    public CurrentUserResponse currentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        assert principal != null;
        return CurrentUserMapper.map(principal.getUser());
    }
}
