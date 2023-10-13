import type { Meta, StoryObj } from '@storybook/react';
import Modal from './Modal';
import React from 'react';

const meta = {
  title: 'common/Modal',
  component: Modal,
  parameters: {
    layout: 'centered',
  },
} satisfies Meta<typeof Modal>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '600px', height: '400px' }}>
        Modal
      </div>
    ),
    closeModal: () => {},
  },
};
