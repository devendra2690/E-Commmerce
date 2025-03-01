package com.online.buy.order.processor.repository;

import com.online.buy.common.code.entity.Reservation;
import com.online.buy.common.code.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BatchUpdateRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchUpdateReservations() {

        String sql = """
                        UPDATE public.reservation
                        SET status = ?, updated_at = ?
                        WHERE status = ?\s
                        AND created_at <= ?\s
                        RETURNING id, user_id, product_id, quantity, status, created_at, updated_at
                   \s""";

        List<Reservation> updatedReservations = jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setString(1, ReservationStatus.INACTIVE.name()); // New status
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Updated timestamp
                    ps.setString(3, ReservationStatus.ACTIVE.name()); // Filter ACTIVE status
                    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusSeconds(30))); // Only reservations created <= 30 seconds ago
                },
                (rs, rowNum) -> new Reservation(
                        rs.getLong("id"),
                        rs.getLong("product_id"),
                        rs.getString("user_id"),
                        rs.getInt("quantity"),
                        ReservationStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                )
        );

        log.info("Reservations marked as INACTIVE: {}", updatedReservations.stream().map(Reservation::getId).toList());


        if(!CollectionUtils.isEmpty(updatedReservations)) {

            String productQuantityUpdate = """
                                        UPDATE public.product\s
                                        SET quantity = quantity + ?, updated_at = ?\s
                                        WHERE id = ?
                                   \s""";

            jdbcTemplate.batchUpdate(productQuantityUpdate, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int addedQuantity = updatedReservations.get(i).getQuantity();
                    long productId = updatedReservations.get(i).getProductId();
                    Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());

                    ps.setInt(1, addedQuantity);
                    ps.setTimestamp(2, updateTime);
                    ps.setLong(3, productId);

                    // ✅ Log the update details
                    log.info("Updating Product ID: {}, Adding Quantity: {}, Timestamp: {}", productId, addedQuantity, updateTime);
                }

                @Override
                public int getBatchSize() {
                    return updatedReservations.size();
                }
            });
        }
    }
}

