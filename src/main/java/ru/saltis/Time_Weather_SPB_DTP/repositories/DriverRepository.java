package ru.saltis.Time_Weather_SPB_DTP.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.saltis.Time_Weather_SPB_DTP.models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    interface AccidentDriverCount {
        Long getAccidentId();
        Long getDriverCount();
    }

    @Modifying
    @Query(value = "TRUNCATE TABLE drivers RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateTable();

    @Query("SELECT d.accident.id AS accidentId, COUNT(d) AS driverCount FROM Driver d WHERE d.accident.id IN :accidentIds GROUP BY d.accident.id")
    java.util.List<AccidentDriverCount> countDriversByAccidentIds(@Param("accidentIds") java.util.List<Long> accidentIds);
}

