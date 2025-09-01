package com.glowapex.service;

import com.glowapex.dto.PasswordChangeRequest;
import com.glowapex.dto.UserProfileDTO;
import com.glowapex.dto.UserProfileUpdateRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.Payment;
import com.glowapex.entity.Role;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.PaymentRepository;
import com.glowapex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ----------------- USER METHODS -----------------

    public UserProfileDTO getProfile(User user) {
        return new UserProfileDTO(user.getId(), user.getEmail(), user.getRole().name());
    }

    public String changePassword(User user, PasswordChangeRequest request) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Old password is incorrect";
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password updated successfully";
    }

    public Map<String, Object> getUserStats(User user) {
        List<Order> orders = orderRepository.findByUserId(user.getId());

        long activeOrders = orders.stream()
                .filter(o -> o != null && o.getStatus() != null && o.getStatus().name().equals("PENDING"))
                .count();

        long completedOrders = orders.stream()
                .filter(o -> o != null && o.getStatus() != null && o.getStatus().name().equals("COMPLETED"))
                .count();

        BigDecimal totalSpent = orders.stream()
                .map(o -> o != null && o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeOrders", activeOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("totalSpent", totalSpent);

        return stats;
    }

    public UserProfileDTO updateProfile(Long id, UserProfileUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);

        return new UserProfileDTO(user.getId(), user.getEmail(), user.getRole().name());
    }

    // ----------------- ADMIN/SUPERADMIN METHODS -----------------

    public List<User> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }

    public User promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    public User demoteToUser(Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == Role.ADMIN) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public long getAllUsersCount() {
        return userRepository.count();
    }

    public long getAllOrdersCount() {
        return orderRepository.count();
    }

    public long getAllPaymentsCount() {
        return paymentRepository.count();
    }

    public List<UserProfileDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserProfileDTO(u.getId(), u.getEmail(), u.getRole().name()))
                .toList();
    }

    // Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Save user (for SUPERADMIN creation)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}