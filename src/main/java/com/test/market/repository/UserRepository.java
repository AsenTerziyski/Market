package com.test.market.repository;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;

import com.test.market.model.projections.UserEntityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.username = :name")
    Optional<UserEntity> findExistingUserByUsername(String name);

    @Query("select u from UserEntity u where u.id = :id")
    Optional<UserEntity> findUserById(Long id);

    //https://www.javacodemonk.com/n-1-problem-in-hibernate-spring-data-jpa-894097b9
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.items items WHERE u.id = :id")
    UserEntity findByIdAndFetchItemsEagerly(@Param("id") Long id);

    @Modifying
    @Query("delete from UserEntity u where u.id = :id")
    void removeUserFromDB(@Param("id") Long id);

    @Query("select u from UserEntity u")
    List<UserEntityView> getAllRegisteredUsers();

}
