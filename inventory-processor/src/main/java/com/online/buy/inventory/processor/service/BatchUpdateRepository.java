package com.online.buy.inventory.processor.service;

import com.online.buy.common.code.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public void batchUpdateReservations(List<Long> reservationIds) {

        String sql = """
                        UPDATE public.reservation\s
                        SET status = ?, updated_at = ?\s
                        WHERE id = ?\s
                   \s""";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Timestamp updateTime = Timestamp.valueOf(LocalDateTime.now());

                ps.setString(1, ReservationStatus.INACTIVE.name());
                ps.setTimestamp(2, updateTime);
                ps.setLong(3, reservationIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return reservationIds.size();
            }
        });

        log.info("Reservations marked as INACTIVE: {}", reservationIds);
    }
}

