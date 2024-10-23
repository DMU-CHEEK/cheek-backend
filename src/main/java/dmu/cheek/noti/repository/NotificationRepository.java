package dmu.cheek.noti.repository;

import dmu.cheek.noti.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.toMember.memberId = :memberId")
    List<Notification> findByToMemberId(long memberId);
}
