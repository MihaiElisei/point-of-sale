package com.pos.repository;

import com.pos.domain.UserRole;
import com.pos.models.Store;
import com.pos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByStore(Store store);
    List<User> findByBranchId(Long branchId);

}
