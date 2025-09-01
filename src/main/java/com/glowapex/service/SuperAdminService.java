package com.glowapex.service;

import com.glowapex.entity.Order;
import com.glowapex.entity.Payment;
import com.glowapex.entity.Role;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ Get all admins
    public List<User> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }

    // ✅ Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ✅ Promote user to admin
    public User promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    // ✅ Demote admin to user
    public User demoteToUser(Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == Role.ADMIN) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }
}