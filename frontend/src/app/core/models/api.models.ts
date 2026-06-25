export interface ResourceConfig {
  key: string;
  title: string;
  description: string;
  endpoint: string;
  fields: FieldConfig[];
  columns: string[];
  columnLabels?: Record<string, string>;
  moneyFields?: string[];
  titleField?: string;
  subtitleField?: string;
  createLabel?: string;
}

export interface FieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'textarea' | 'select';
  required?: boolean;
  options?: string[];
  optionsEndpoint?: string;
  optionValueKey?: string;
  optionLabelKey?: string;
}

export type ApiRecord = Record<string, unknown>;
