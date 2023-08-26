
package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestAnswerRepository extends JpaRepository<RequestAnswer, Integer>  {
    public List<RequestAnswer> findAllByRequestId(int requestId);
}


