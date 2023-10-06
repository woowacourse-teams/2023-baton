export interface GetAlarmResponse {
  data: Alarm[];
}

export interface Alarm {
  alarmId: number;
  title: string;
  message: string;
  alarmType: string;
  referencedId: number;
  isRead: boolean;
  createdAt: string;
}
