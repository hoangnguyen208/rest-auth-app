package com.restphotoapp.restphotoapp.repositories;

import java.util.List;

import com.restphotoapp.restphotoapp.models.DAL.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {
    User findByEmail(String email);

    User findByUserId(String userId);

    User findUserByEmailVerificationToken(String token);

    // Native SQL query
    @Query(value = "select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", nativeQuery = true)
    Page<User> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

    // ?1 and ?2 in nativeQuery means 1st parameter and 2nd parameter in the defined
    // function
    @Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
    List<User> findUsersByFirstName(String firstName);

    // :lastName needs to be the same with @Param("lastName")
    @Query(value = "select * from Users u where u.last_name = :lastName", nativeQuery = true)
    List<User> findUsersByLastName(@Param("lastName") String lastName);

    // the sign % means it does not matter start with or end with any character
    // e.g. %:keyword% means it does not matter the keyword starts with or end with
    // as long as it contains the keyword
    @Query(value = "select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
    List<User> findUsersByKeyword(@Param("keyword") String keyword);

    @Query(value = "select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUsersFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.user_id = :userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId);

    // JPQL query does not need to set value and no need to set nativeQuery to true
    // 'from User' should match DAL.User
    @Query("select user from User user where user.userId = :userId")
    User findUserEntityByUserId(@Param("userId") String userId);

    @Query("select user.firstName, user.lastName from User user where user.userId = :userId")
    List<Object[]> getUserFullNameById(@Param("userId") String userId);

    // @Transactional is to prevent changes in db if errors happen
    // can put in REST controllers or services to prevent from
    // first stages of API calls
    @Transactional
    @Modifying
    @Query("update User u set u.emailVerificationStatus = :emailVerificationStatus where u.userId = :userId")
    void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId);
}