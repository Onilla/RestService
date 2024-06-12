package com.homework.repository;

import com.homework.entity.Position;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends Repository<Position, Long>{
    List<Long> findPositionIdByUserId(Long id);
}
