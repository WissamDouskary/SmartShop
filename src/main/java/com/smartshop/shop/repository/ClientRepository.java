package com.smartshop.shop.repository;

import com.smartshop.shop.model.Client;
import com.smartshop.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    boolean existsByEmail(String email);

    Client findClientByUser_Id(String userId);

    String user(User user);
}
