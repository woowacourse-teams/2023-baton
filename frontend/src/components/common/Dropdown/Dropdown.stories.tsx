import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';
import Dropdown from './Dropdown';
import Button from '../Button/Button';

const meta = {
  title: 'common/Dropdown',
  component: Dropdown,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
} satisfies Meta<typeof Dropdown>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    trigger: <Button colorTheme="RED">Button</Button>,
    children: (
      <ul style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '180px', padding: '10px' }}>
        <li>첫 번째 리스트</li>
        <li>두 번째 리스트</li>
        <li>세 번째 리스트</li>
      </ul>
    ),
    isDropdownOpen: true,
    gapFromTrigger: '40px',
    onClose: () => {},
  },
};
