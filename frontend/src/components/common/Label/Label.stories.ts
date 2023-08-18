import type { Meta, StoryObj } from '@storybook/react';
import Label from './Label';

const meta = {
  title: 'common/Label',
  component: Label,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
} satisfies Meta<typeof Label>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '리뷰 상태 라벨',
    colorTheme: 'RED',
    fontSize: '14px',
    fontWeight: 400,
  },
};
