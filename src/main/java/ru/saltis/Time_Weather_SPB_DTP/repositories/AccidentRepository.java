package ru.saltis.Time_Weather_SPB_DTP.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.saltis.Time_Weather_SPB_DTP.models.Accident;

@Repository
public interface AccidentRepository extends JpaRepository<Accident, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE accidents RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateTable();
}

