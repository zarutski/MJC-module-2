package com.epam.esm.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private BigDecimal cost;
    @CreationTimestamp
    @Column(name = "purchase_date", updatable = false)
    private LocalDateTime date;
    @Column(name = "user_id")
    private Long userId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "gift_certificate_has_orders",
            joinColumns = @JoinColumn(name = "orders_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id", nullable = false)
    )

    private List<Certificate> certificateList;
}