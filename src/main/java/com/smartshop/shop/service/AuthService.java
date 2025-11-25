package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.dto.responseDTO.UserResponseDTO;
import com.smartshop.shop.enums.CustomerTier;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.exception.UnauthorizedException;
import com.smartshop.shop.mapper.ClientMapper;
import com.smartshop.shop.mapper.UserMapper;
import com.smartshop.shop.model.Client;
import com.smartshop.shop.model.User;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;

    public UserResponseDTO login(String username, String password) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public ClientResponseDTO register(ClientRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUser().getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Client client = clientMapper.toEntity(dto);
        User user = client.getUser();

        String hashedPassword = BCrypt.hashpw(dto.getUser().getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setRole(Role.CLIENT);

        client.setUser(user);
        client.setCustomerTier(CustomerTier.BASIC);
        client.setTotalOrders(0);

        client = clientRepository.save(client);

        return clientMapper.toResponse(client);
    }
}
