export interface GetAlarmResponse {
  data: Alarm[];
}

export interface Alarm {
  alarmId: number;
  title: string;
  message: string;
  url: string;
  isRead: boolean;
  createdAt: string;
}
