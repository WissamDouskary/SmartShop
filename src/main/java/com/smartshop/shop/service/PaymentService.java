package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.PaymentRequestDTO;
import com.smartshop.shop.dto.responseDTO.PaymentResponseDTO;
import com.smartshop.shop.enums.PaymentStatus;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.PaymentMapper;
import com.smartshop.shop.model.Order;
import com.smartshop.shop.model.Payment;
import com.smartshop.shop.repository.OrderRepository;
import com.smartshop.shop.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    private final double ESPECES_LIMIT = 20000;

    @Transactional
    public PaymentResponseDTO payOrder(PaymentRequestDTO dto){
        Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(
                () -> new ResourceNotFoundException("Aucun order avec id: "+dto.getOrderId())
        );

        Payment payment = paymentMapper.toEntity(dto);

        payment.setPaymentStatus(PaymentStatus.EN_ATTENTE);

        int nextPaymentNumber = paymentRepository.countByOrderId(dto.getOrderId()) + 1;
        payment.setPaymentNumber(nextPaymentNumber);

        if(isOrderFullPaid(order)){
            throw new BusinessException("Order is Full paid!");
        }

        if(order.getMontantRestant() < payment.getMontant()){
            throw new BusinessException("price you gived is more than montant restant!");
        }

        switch (dto.getTypePayment()){
            case ESPECES -> {
                if(dto.getMontant() >= ESPECES_LIMIT){
                    throw new BusinessException("You have reached the limite of espece pay! our especes limite is: "+ESPECES_LIMIT);
                }
                payment.setPaymentStatus(PaymentStatus.ENCAISSE);
                payment.setReference("RECU-"+ UUID.randomUUID().toString().subSequence(0, 5));
            }
            case CHEQUE -> {
                if(dto.getDateEcheance() == null){
                    throw new BusinessException("You should enter a valid echeance Date in CHEQUE types!");
                }

                LocalDate dateEch = LocalDate.parse(dto.getDateEcheance());

                if(!dateEch.isAfter(LocalDate.now())){
                    throw new BusinessException("You should enter a valid echeance date after today!");
                }
                payment.setPaymentStatus(PaymentStatus.ENCAISSE);
                payment.setReference("CHQ-"+ UUID.randomUUID().toString().subSequence(0, 5));
            }
            case VIREMENT -> {
                payment.setPaymentStatus(PaymentStatus.ENCAISSE);
                payment.setReference("VIR-"+ UUID.randomUUID().toString().subSequence(0, 7));
            }
        }

        payment.setOrder(order);
        payment.setDatePayment(LocalDate.now());

        Payment savedPayment =  paymentRepository.save(payment);

        calculatingOrderMontantRestant(order, savedPayment);

        return paymentMapper.toResponse(savedPayment);
    }

    public boolean isOrderFullPaid(Order order){
        return order.getMontantRestant() == 0;
    }

    @Transactional
    public void calculatingOrderMontantRestant(Order order, Payment savedPayment){
        order.setMontantRestant(order.getMontantRestant() - savedPayment.getMontant());
        order.getClient().setTotalSpent(order.getClient().getTotalSpent() + savedPayment.getMontant());
        orderRepository.save(order);
    }
}
