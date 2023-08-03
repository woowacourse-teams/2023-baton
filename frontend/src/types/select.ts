export interface SelectOption<T extends string | number> {
  value: T;
  label: string;
  selected: boolean;
}

export type ListSelectOption = SelectOption<string | number>;
