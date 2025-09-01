package com.glowapex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerName;   // Example: PayPal, Stripe, Razorpay
    private String alias;          // Short name / reference
    private Double balance;        // Current balance with provider
    private String status;         // ACTIVE / INACTIVE
    private String apiKey;         // API Key used for integration
}