import type { Meta, StoryObj } from '@storybook/react';
import Button from './Button';

const meta = {
  title: 'common/button',
  component: Button,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
} satisfies Meta<typeof Button>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '버튼',
    colorTheme: 'RED',
    fontSize: '18px',
    fontWeight: 400,
    type: 'button',
  },
};
