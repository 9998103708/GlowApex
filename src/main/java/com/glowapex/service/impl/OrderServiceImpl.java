package com.glowapex.service.impl;

import com.glowapex.dto.OrderRequest;
import com.glowapex.entity.Order;
import com.glowapex.entity.OrderStatus;
import com.glowapex.entity.ServiceProduct;
import com.glowapex.entity.User;
import com.glowapex.repository.OrderRepository;
import com.glowapex.repository.ServiceProductRepository;
import com.glowapex.repository.UserRepository;
import com.glowapex.service.EmailService;
import com.glowapex.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ServiceProductRepository serviceProductRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Order placeOrder(OrderRequest request) {
        // 1. Validate service
        Optional<ServiceProduct> serviceOpt = serviceProductRepository.findByServiceName(request.getServiceName());
        if (serviceOpt.isEmpty()) {
            throw new RuntimeException("Invalid service name: " + request.getServiceName());
        }

        // 2. Check if user exists or create one
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    String randomPassword = UUID.randomUUID().toString().substring(0, 8);
                    User newUser = new User();
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(randomPassword));
                    newUser.setRole("USER");
                    userRepository.save(newUser);
                    emailService.sendEmail(newUser.getEmail(), "Welcome to GlowApex",
                            "Your account has been created.\nEmail: " + newUser.getEmail() + "\nPassword: " + randomPassword);
                    return newUser;
                });

        // 3. Calculate price (get correct quantity and price)
        ServiceProduct product = serviceOpt.get();
        double price = product.getPackages().stream()
                .filter(p -> p.getName().equalsIgnoreCase(request.getPackageName()))
                .flatMap(p -> p.getQuantities().stream())
                .filter(q -> q.getAmount() == request.getQuantity())
                .findFirst()
                .map(q -> q.getPrice() - q.getDiscount())
                .orElseThrow(() -> new RuntimeException("Invalid package or quantity selected."));

        // 4. Create and save order
        Order order = new Order();
        order.setUser(user);
        order.setServiceName(request.getServiceName());
        order.setQuantity(request.getQuantity());
        order.setPrice(price);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return orderRepository.findByUserId(user.getId());
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (currentUser.getRole().equals("USER") && !order.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied. You can only cancel your own orders.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}