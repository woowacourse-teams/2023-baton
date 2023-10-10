export interface GetNotificationResponse {
  data: Notification[];
}

export interface Notification {
  notificationId: number;
  title: string;
  message: string;
  notificationType: string;
  referencedId: number;
  isRead: boolean;
  createdAt: string;
}
