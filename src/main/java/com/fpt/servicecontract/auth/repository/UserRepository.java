package com.fpt.servicecontract.auth.repository;

import com.fpt.servicecontract.auth.dto.UserInterface;
import com.fpt.servicecontract.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    @Query(value = "select * from users where id = :id", nativeQuery = true)
    User searchByUserId(@Param("id") String id);

    Optional<User> findByEmailAndStatus(String email, String status);

    @Query(value = """
            SELECT u.id,u.name, u.email, u.address,
                                      u.identification_number as identificationNumber, u.status, u.department, u.phone, u.position, u.avatar,
                                      CONCAT('[', GROUP_CONCAT(up.permissions SEPARATOR ','), ']') AS permissions, u.dob
                               FROM users u
                               JOIN user_permissions up ON u.id = up.user_id where
                                               (lower(u.name) like lower(:name) or :name is null)
                                               and (lower(u.email) like lower(:email) or :email is null)
                                               and (lower(u.address) like lower(:address) or :address is null)
                                               and (lower(u.identification_number) like lower(:identificationNumber) or :identificationNumber is null)
                                               and (lower(u.status) = :status or :status is null)
                                               and (lower(u.department) like lower(:department) or :department is null)
                                               and (lower(u.phone) like lower(:phoneNumber) or :department is null)
                                               and (lower(u.position) like lower(:position) or :department is null)
                                               and (lower(u.role) like lower(:role) or :role is null)
                               GROUP BY u.id, u.name, u.email, u.address,
                                        u.identification_number, u.status, u.department, u.phone, u.position
                               order by u.created_date desc
                """
            , nativeQuery = true)
    Page<UserInterface> search(@Param("name") String name,
                               @Param("email") String email,
                               @Param("address") String address,
                               @Param("identificationNumber") String identificationNumber,
                               @Param("status") String status,
                               @Param("department") String department,
                               @Param("phoneNumber") String phoneNumber,
                               @Param("position") String position,
                               @Param("role") String role,
                               Pageable pageable);

    @Query(value = """
            SELECT u.name, u.email, CONCAT('[', GROUP_CONCAT(DISTINCT up.permissions SEPARATOR ','), ']') AS permissions
                               FROM users u
                               JOIN user_permissions up ON u.id = up.user_id where
                                            (permissions like lower(:permission) or :permission is null)
                               GROUP BY u.name, u.email
                """
            , nativeQuery = true)
    Page<UserInterface> getUserWithPermission(
            @Param("permission") String permission,
            Pageable pageable);


    @Query(value = """
            SELECT u.email, CONCAT('[', GROUP_CONCAT(DISTINCT up.permissions SEPARATOR ','), ']') AS permissions,
                   u.id, u.name, u.address, u.phone, u.position, u.identification_number as identificationNumber, u.department
                               FROM users u
                               JOIN user_permissions up ON u.id = up.user_id where
                                            (permissions like lower(:permission) or :permission is null)
                               GROUP BY u.email, u.id, u.name, u.address, u.phone, u.position, u.identification_number, u.department
                """
            , nativeQuery = true)
    List<UserInterface> getUserWithPermissionList(
            @Param("permission") String permission);

    @Query(value = """
                    SELECT c.created_by, SUM(c.value) AS numberSales
                        FROM
                            contract c
                    WHERE
                        c.mark_deleted = 0
                        AND (c.created_by in (:emails))
                        AND (c.created_date between DATE_FORMAT(CURDATE(), '%Y-%m-01') and LAST_DAY(CURDATE()))
                        AND (c.status = 'SUCCESS')
                    GROUP BY c.created_by
            """, nativeQuery = true)
    List<Object[]> getSaleAndNumberSales(List<String> emails);


    @Query(value = """
                    SELECT SUM(c.value) AS numberSales
                        FROM
                            contract c
                    WHERE
                        c.mark_deleted = 0
                        and (month(pl.created_date) = :monthSearch or :monthSearch is null)
                        and (year(pl.created_date) = :yearSearch or :yearSearch is null)
                        AND (c.status = 'SUCCESS')
            """, nativeQuery = true)
    Double getTotalNumberSales(Integer monthSearch, Integer yearSearch);
}
