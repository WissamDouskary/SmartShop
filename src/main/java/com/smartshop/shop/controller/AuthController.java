package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.requestDTO.UserLoginRequestDTO;
import com.smartshop.shop.dto.requestDTO.UserRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.dto.responseDTO.UserResponseDTO;
import com.smartshop.shop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @RequestBody @Valid UserLoginRequestDTO loginRequest,
            HttpServletRequest request
    ) {
        UserResponseDTO user = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

        HttpSession session = request.getSession();
        session.setAttribute("USER_ID", user.getId());
        session.setAttribute("USER_ROLE", user.getRole());

        return ResponseEntity.ok(ApiResponse.success(user, "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }
}
