package ru.saltis.Time_Weather_SPB_DTP.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saltis.Time_Weather_SPB_DTP.models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}

