export const getDatetime = (date: Date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  const datetime = `${year}-${month}-${day}T${hours}:${minutes}`;

  return datetime;
};

export const getDayLastTime = (date: Date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  const datetime = `${year}-${month}-${day}T23:59`;

  return datetime;
};

export const addDays = (date: Date, days: number): Date => {
  const clone = new Date(date);
  clone.setDate(date.getDate() + days);

  return clone;
};

export const addHours = (date: Date, hours: number): Date => {
  const clone = new Date(date);
  clone.setHours(date.getHours() + hours);

  return clone;
};

export const isPastTime = (date: Date) => {
  const nowDate = new Date();

  return date < nowDate;
};
