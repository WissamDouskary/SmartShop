package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.enums.CustomerTier;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.mapper.ClientMapper;
import com.smartshop.shop.model.Client;
import com.smartshop.shop.model.User;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO dto) {
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

        client.setCustomerTier(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.valueOf(0));

        client.setUser(user);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toResponse(savedClient);
    }
}
