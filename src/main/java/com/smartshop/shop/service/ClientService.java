package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.enums.CustomerTier;
import com.smartshop.shop.enums.OrderStatus;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.ClientMapper;
import com.smartshop.shop.model.Client;
import com.smartshop.shop.model.Order;
import com.smartshop.shop.model.User;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public ClientResponseDTO updateClient(String clientId, ClientRequestDTO newClient){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Aucun Client avec id: "+clientId)
        );
        clientMapper.updateClientFromDto(newClient, client);

        Client savedClient = clientRepository.save(client);

        return clientMapper.toResponse(savedClient);
    }

    public void deleteClient(String clientId){
        clientRepository.deleteById(clientId);
    }

    @Transactional
    public void updateClientStatistics(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        List<Order> confirmedOrders = client.getOrderList().stream()
                .filter(order -> order.getStatus() == OrderStatus.CONFIRMED)
                .toList();

        client.setTotalOrders(confirmedOrders.size());

        BigDecimal totalSpent = confirmedOrders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        client.setTotalSpent(totalSpent);

        if (!confirmedOrders.isEmpty()) {
            Optional<LocalDate> firstDate = confirmedOrders.stream()
                    .map(Order::getDate)
                    .min(Comparator.naturalOrder());

            Optional<LocalDate> lastDate = confirmedOrders.stream()
                    .map(Order::getDate)
                    .max(Comparator.naturalOrder());

            client.setFirstOrderDate(firstDate.orElse(null));
            client.setLastOrderDate(lastDate.orElse(null));
        } else {
            client.setFirstOrderDate(null);
            client.setLastOrderDate(null);
        }

        updateLoyaltyTier(client);

        clientRepository.save(client);
    }

    private void updateLoyaltyTier(Client client) {
        int count = client.getTotalOrders();
        BigDecimal spent = client.getTotalSpent();

        if (count >= 20 || spent.compareTo(new BigDecimal("15000")) >= 0) {
            client.setCustomerTier(CustomerTier.PLATINUM);
        } else if (count >= 10 || spent.compareTo(new BigDecimal("5000")) >= 0) {
            client.setCustomerTier(CustomerTier.GOLD);
        } else if (count >= 3 || spent.compareTo(new BigDecimal("1000")) >= 0) {
            client.setCustomerTier(CustomerTier.SILVER);
        } else {
            client.setCustomerTier(CustomerTier.BASIC);
        }
    }
}
