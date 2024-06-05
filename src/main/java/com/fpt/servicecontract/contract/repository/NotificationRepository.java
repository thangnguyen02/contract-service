package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = """
        select u.id, u.title, u.message, u.created_by, u.created_date, u.marked_deleted, u.mark_read from notification u
        where 1=1
        and u.marked_deleted = false
    """, nativeQuery = true)
    List<Object[]> getAllNotificationBy();
}
