package dmu.cheek.noti.repository;

import dmu.cheek.noti.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
