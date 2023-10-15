package dev.wms.pwrapi.utils.notifications;

public interface NotificationService {

    <T> void notify(String destination, T info);

}
