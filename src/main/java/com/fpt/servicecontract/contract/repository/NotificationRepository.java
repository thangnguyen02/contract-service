package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = """
        select u.id, u.title, u.message, u.sender_id, u.created_date, u.marked_deleted,
               u.mark_read, u.recipient_id, ei.object_id, ei.entity_type, ei.description,
               sender.name, recipient.name
        from notification u
        join entity_id ei on u.entity_id = ei.id
        join users sender on u.sender_id = sender.id
        join users recipient on u.recipient_id = recipient.id
        where 1=1
        and u.marked_deleted = false
        and (u.recipient_id = :recipientId)
    """, nativeQuery = true)
    List<Object[]> getAllNotificationBy(@Param("recipientId") String recipientId);
}
